package com.fosusstart.currency.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fosusstart.currency.R;
import com.fosusstart.currency.adapters.ClickListener;
import com.fosusstart.currency.adapters.CurrenciesDataAdapter;
import com.fosusstart.currency.objects.CurrenciesDto;
import com.fosusstart.currency.objects.CurrencyItem;
import com.fosusstart.currency.utility.SharedPreferencesUtil;
import com.fosusstart.currency.utility.ToastUtil;
import com.fosusstart.currency.utility.alarms.AlarmManagerUtil;
import com.fosusstart.currency.utility.alarms.AlarmObservable;
import com.fosusstart.currency.utility.moshi.MoshiUtil;
import com.fosusstart.currency.utility.requests.NetClient;
import com.fosusstart.currency.utility.requests.RequestCallback;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Фрагмент, отображающий список валют
 */
public class CurrenciesListFragment extends Fragment implements Observer {

    private final String itemsKey = "currencyItems"; //ключ, по которому будем доставать сохраненные в bundle валюты
    private ArrayList<CurrencyItem> currencyItems = new ArrayList<>(); //список валют
    private CurrenciesDataAdapter adapter;

    public CurrenciesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutInflater = inflater.inflate(R.layout.fragment_currency_item_list, container, false);

        //настройка recyclerview (списка)
        adapter = new CurrenciesDataAdapter(currencyItems);
        //обработка клика по валюте
        adapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                CurrencyItem clickedItem = currencyItems.get(position);
                //открываем фрагмент конвентор валют
                if (getFragmentManager() != null) {
                    ConverterDialogFragment fragment = new ConverterDialogFragment(clickedItem.getCharCode(), clickedItem.getValue());
                    fragment.show(getFragmentManager(), "CONVERTER_DIALOG");
                }
            }
        });

        final RecyclerView recyclerView = layoutInflater.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(layoutInflater.getContext()));
        recyclerView.setAdapter(adapter);

        ArrayList<CurrencyItem> memoryItems = getItemsFromMemory(savedInstanceState); //выгружаем из памяти список валют
        if (memoryItems != null) { //если не пуста
            currencyItems.addAll(memoryItems);
        }
        else { //иначе делаем http запрос на получение списка валют
            if (getActivity() != null) {
                refreshItems(currencyItems, adapter, getActivity());
            }
        }

        //добавляем observer в текущий фрагмент, чтобы обновить список в update
        AlarmObservable.getInstance().addObserver(this);

        //создаем периодическое обновление валют
        if (getContext() != null) {
            AlarmManagerUtil alarmManagerUtil = new AlarmManagerUtil();
            alarmManagerUtil.makeAlarm(getContext());
        }

        //актвитация меню опций (кнопка обновить)
        setHasOptionsMenu(true);
        //действие при свайпе вниз
        final SwipeRefreshLayout pullToRefresh = layoutInflater.findViewById(R.id.list_refresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getActivity() != null) {
                    refreshItems(currencyItems, adapter, getActivity()); //обновляем курс валют
                }
                pullToRefresh.setRefreshing(false);
            }
        });

        return layoutInflater;
    }

    //метод загружает список валют с api
    private void refreshItems(final ArrayList<CurrencyItem> items, final CurrenciesDataAdapter adapter, @NonNull final Activity activity) {
        //делаем запрос на получение json с помощью библиотеки okhhtp
        NetClient.getNetClient().doGetRequest("https://www.cbr-xml-daily.ru/daily_json.js", new RequestCallback() {
            //коллбэк при успешном результате запрсоа (200)
            @Override
            public void onResponse(String json) {
                MoshiUtil moshiUtil = new MoshiUtil();
                CurrenciesDto dto = moshiUtil.fromJsonToObject(json, CurrenciesDto.class); //десериализируем json с помощью библиотеки moshi
                final SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(activity);
                sharedPreferencesUtil.saveJson(json);

                items.clear(); //если список валют обновляется, необходимо очистить список перед добавлением нового
                items.addAll(dto.getCurrenciesList().values()); //добавляем полученные валюты в список recyclerview

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged(); //уведомляем об необходимости обновить список
                    }
                });
            }

            //коллбэк при http ошибке (300;500)
            @Override
            public void onError(String message) {
                ToastUtil.showToast(activity, R.string.error_get_currencies);
            }

            //коллбэк на случай, если возникла ошибка на стороне клиента
            @Override
            public void onFailure(String message) {
                ToastUtil.showToast(activity, R.string.error_get_currencies_connection);
            }
        });
    }

    //метод возвращает список валют из памяти, если память не пуста
    private ArrayList<CurrencyItem> getItemsFromMemory(Bundle savedInstanceState) {
        //savedInstanceState не null тогда, когда вызывается onSaveInstanceState - это происходит при перевороте экрана
        if (savedInstanceState != null) {
            return savedInstanceState.getParcelableArrayList(itemsKey);
        }

        //данные в SharedPreferences хранятся между перезапусками, поэтому запустится этот код, если приложение перезапущено
        if (getActivity() != null) {
            SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getActivity());
            String json = sharedPreferencesUtil.getJson(); //получаем сохраненный json валют
            if (!json.isEmpty()) { //если список валют сохранялся
                CurrenciesDto dto = new MoshiUtil().fromJsonToObject(json, CurrenciesDto.class);
                return new ArrayList<>(dto.getCurrenciesList().values());
            }
        }

        return null; //если в памяти ничего не было
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList(itemsKey, currencyItems);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //нажата кнопка меню "обновить" в правом верхнем углу
        if (item.getItemId() == R.id.menu_refresh) {
            if (getActivity() != null) {
                refreshItems(currencyItems, adapter, getActivity());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //обновление списка, если запрос на получение валют был сделан в бэкграунде
    @Override
    public void update(Observable observable, Object json) {
        MoshiUtil moshiUtil = new MoshiUtil();
        CurrenciesDto dto = moshiUtil.fromJsonToObject((String) json, CurrenciesDto.class);
        currencyItems.clear();
        currencyItems.addAll(dto.getCurrenciesList().values());
        adapter.notifyDataSetChanged();
    }
}