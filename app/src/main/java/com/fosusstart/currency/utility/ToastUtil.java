package com.fosusstart.currency.utility;

import android.app.Activity;
import android.widget.Toast;

/**
 * Класс отображает Toast сообщение, используя UI поток
 * */
public class ToastUtil {

    //отображение строки в Toast сообщении
    public static void showToast(final Activity activity, final String message) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //отображение строки из ресурсов в Toast сообщении
    public static void showToast(final Activity activity, final int resId) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
