package com.fosusstart.currency.utility.requests;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
* Класс инкапсулирует библиотеку okhhtp с целью
 * осуществления запросов через один простой метод
* */
public class NetClient {

    private static NetClient netClient;
    private NetClient(){
        client = initOkHttpClient();
    }
    public final OkHttpClient client;

    private OkHttpClient initOkHttpClient(){
        return new OkHttpClient.Builder()
                .build();
    }
    public static NetClient getNetClient(){
        if(netClient == null){
            netClient = new NetClient();
        }
        return netClient;
    }

    //осуществление GET запроса
    public void doGetRequest(String url, final RequestCallback callback){
        Request request = new Request.Builder().url(url).build();
        Call call = getNetClient().initOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBody = response.body().string(); //получаем тело запроса

                    if (response.isSuccessful()) { //code [200;300]
                        callback.onResponse(responseBody);
                    }
                    else { //code (300;500)
                        callback.onError(responseBody);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                callback.onFailure(e.getMessage());
            }
        });
    }
}
