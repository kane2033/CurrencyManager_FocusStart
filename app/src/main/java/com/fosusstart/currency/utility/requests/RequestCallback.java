package com.fosusstart.currency.utility.requests;

/**
* Интерфейс содержит коллбэк для типичных результатов
 * запросов
* */
public interface RequestCallback {
    void onResponse(String value); //[200;300]
    void onError(String message); //(300;500)
    void onFailure(String message); //request failed
}
