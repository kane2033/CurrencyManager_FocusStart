package com.fosusstart.currency.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fosusstart.currency.R;
import com.fosusstart.currency.utility.ToastUtil;

public class CurrencyConverterFragment extends Fragment {

    //ключи для bundle
    private final static String charCodeKey = "charCode";
    private final static String valueKey = "value";

    /**
    * Метод создает новый экземпляр фрагмента (конвентора валют)
    * @param charCode Буквенный код валюты
     * @param value Стоимость валюты
     * @return Новый экземпляр конвертора валют
    * */
    public static CurrencyConverterFragment newInstance(String charCode, double value) {
        CurrencyConverterFragment fragment = new CurrencyConverterFragment();
        Bundle args = new Bundle();
        args.putString(charCodeKey, charCode);
        args.putDouble(valueKey, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layoutInflater = inflater.inflate(R.layout.fragment_currency_converter, container, false);
        final Activity activity = getActivity();

        //получаем буквенным код и стоимость валюты из выбранной валюты
        Bundle args = getArguments();
        final String charCode;
        final double value;
        if (args != null) {
            charCode = args.getString(charCodeKey, "");
            value = args.getDouble(valueKey, 1);
        }
        else {
            charCode = "";
            value = 1;
        }

        final EditText rublesInputView = layoutInflater.findViewById(R.id.converter_input_rub); //поле ввода суммы в рублях

        final TextView resultCurrencyCodeView = layoutInflater.findViewById(R.id.converter_result_currency);
        resultCurrencyCodeView.setText(charCode); //отображаем, в какую валюту будем переводить
        final TextView resultOutputView = layoutInflater.findViewById(R.id.converter_result_output); //поле отображения результата конвертации

        final ImageView convertButton = layoutInflater.findViewById(R.id.converter_button_convert); //кнопка конвертации
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //конвертация рублей в указанную валюту при клике на кнопку
                String inputString = rublesInputView.getText().toString();
                String precision = "%.4f";
                if (!inputString.isEmpty()) {
                    try {
                        double rublesInput = Double.parseDouble(inputString);
                        double result = rublesInput / value; //переводим валюту к одной единице (номиналу)
                        resultOutputView.setText(String.format(precision, result)); //выводим ответ с точностью до 4х
                    }
                    catch (NumberFormatException e) { //если получен неверный формат строки
                        ToastUtil.showToast(activity, R.string.error_converter_parse);
                    }
                }
                else { //если юзер ничего не ввёл
                    ToastUtil.showToast(activity, R.string.error_converter_empty);
                }
            }
        });

        return layoutInflater;
    }
}
