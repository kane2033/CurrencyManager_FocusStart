package com.fosusstart.currency.utility.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Класс отвечает за активацию регулярного обновления валют
 * (запуск AlarmManager)
 * */
public class AlarmManagerUtil {

    public static String getIntentActionString() {
        return "update_currencies";
    }

    //всегда возвращает интент на AlarmReceiver
    private Intent getAlarmReceiverIntent(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(getIntentActionString());
        return intent;
    }

    //создание регулярного обновления через AlarmManager
    public void makeAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final long timeInterval = AlarmManager.INTERVAL_DAY; //обновление каждый день
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, getAlarmReceiverIntent(context), 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + timeInterval,
                timeInterval, pendingIntent);
    }

    //отмена регулярного обновления
    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getService(context, 0, getAlarmReceiverIntent(context),
                        PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
