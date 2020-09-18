package com.fosusstart.currency.utility.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fosusstart.currency.utility.SharedPreferencesUtil;
import com.fosusstart.currency.utility.requests.NetClient;
import com.fosusstart.currency.utility.requests.RequestCallback;


/**
* BroadcastReceiver получает от AlarmManager сигнал каждый временной промежуток,
 * в методе onReceive реализация действия при получении сигнала
* */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(AlarmManagerUtil.getIntentActionString())) { //если получили intent именно от AlarmManager
            //делаем запрос на обновление списка валют
            NetClient.getNetClient().doGetRequest("https://www.cbr-xml-daily.ru/daily_json.js", new RequestCallback() {
                //коллбэк при успешном результате запрсоа (200)
                @Override
                public void onResponse(final String json) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            //сохраняем в sharedPreferences полученный запрос
                            SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
                            sharedPreferencesUtil.saveJson(json);
                            /*
                            * передаем сохраненный json классу observable, который передаст
                            * новый курс валют в метод update фрагмента currency_item_list
                            * */
                            AlarmObservable.getInstance().updateValue(json);
                        }
                    });
                }

                //коллбэк при http ошибке (300;500)
                @Override
                public void onError(String message) {
                    Log.d("ALARM_ERR", "Failed to refresh items in background. Message: " + message);
                }

                //коллбэк на случай, если возникла ошибка на стороне клиента
                @Override
                public void onFailure(String message) {
                    Log.d("ALARM_ERR", "Failed to refresh items in background. Message: " + message);
                }
            });
        }
    }
}
