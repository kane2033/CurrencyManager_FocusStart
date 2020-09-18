package com.fosusstart.currency.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.fosusstart.currency.R;
import com.fosusstart.currency.utility.ToastUtil;


/**
* Диалоговый фрагмент, отвечающий за конвертор валют
* */
public class ConverterDialogFragment extends DialogFragment {

    private EditText rublesInputView;
    private TextView resultOutputView;

    private String charCode;
    private double value;

    public ConverterDialogFragment(String charCode, double value) {
        this.charCode = charCode;
        this.value = value;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View view = activity.getLayoutInflater().inflate(R.layout.fragment_currency_converter, null); //xml разметка фрагмента

        //инициализация полей
        rublesInputView = view.findViewById(R.id.converter_input_rub); //поле ввода суммы в рублях

        TextView resultCurrencyCodeView = view.findViewById(R.id.converter_result_currency);
        resultCurrencyCodeView.setText(charCode); //отображаем, в какую валюту будем переводить

        resultOutputView = view.findViewById(R.id.converter_result_output); //поле отображения результата конвертации

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view)
                .setPositiveButton(R.string.converter_convert, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //реализация нажатия кнопки конвертирования находится в onResume
                    }
                })
                .setNegativeButton(R.string.converter_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); //закрываем диалог
                    }
                });
        return builder.create();
    }

    /**
     * Реализация нажатия кнопки находится в методе onResume, так как
     * в противном случае DialogFragment закроется при нажатии кнопки "Конвертировать" (positiveButton)
     * */
    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog alertDialog = (AlertDialog)getDialog();
        if (alertDialog != null) {
            alertDialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Activity activity = getActivity();
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
        }
    }
}
