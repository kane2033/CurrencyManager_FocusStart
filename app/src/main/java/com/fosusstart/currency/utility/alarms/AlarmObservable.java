package com.fosusstart.currency.utility.alarms;


import java.util.Observable;

/**
 * Класс обновляет валюты при получении сигнала от BroadcastReceiver:
 * в фрагмент currency_item_list возвращается json с курсом валют
 * */
public class AlarmObservable extends Observable {
    //singleton
    private static AlarmObservable instance = new AlarmObservable();

    public static AlarmObservable getInstance() {
        return instance;
    }

    private AlarmObservable() {
    }

    //возвращает полученный json в update метод фрагмента
    public void updateValue(Object data) {
        synchronized (this) {
            setChanged();
            notifyObservers(data);
        }
    }
}
