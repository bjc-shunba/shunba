package com.baidu.shunba.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class GsonUtils {
    public static final Gson gson = new Gson();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static int getIntValue(JsonObject json, String propName, int defaultValue) {
        JsonElement jsonElement = json.get(propName);

        if (null == jsonElement) {
            return defaultValue;
        }

        return jsonElement.getAsInt();
    }

    public static String getStringValue(JsonObject json, String propName) {
        return getStringValue(json, propName, null);
    }

    public static String getStringValue(JsonObject json, String propName, String defaultValue) {
        JsonElement jsonElement = json.get(propName);

        if (null == jsonElement) {
            return defaultValue;
        }

        return jsonElement.getAsString();
    }

}