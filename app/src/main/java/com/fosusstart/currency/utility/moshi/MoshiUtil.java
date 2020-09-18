package com.fosusstart.currency.utility.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class MoshiUtil {

    private final Moshi moshi;

    public MoshiUtil() {
        moshi = new Moshi.Builder()
                .add(Date.class, new Rfc3339DateJsonAdapter().nullSafe())
                .build();
    }

    public <T> List<T> fromJsonToArrayList(String json, Class<T> clazz) {
        Type type = Types.newParameterizedType(List.class, clazz);
        JsonAdapter<List<T>> adapter = moshi.adapter(type);
        try {
            return adapter.fromJson(json);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T fromJsonToObject(String json, Class<T> clazz) {
        JsonAdapter<T> jsonAdapter = moshi.adapter(clazz);
        try {
            return jsonAdapter.fromJson(json);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getJsonFieldFromJson(String field, String json) {
        try {
            return new JSONObject(json).getString(field);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
