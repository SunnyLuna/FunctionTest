package com.example.commonlibs.utils;


import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class JsonUtils {


    private static final String TAG = "-----JsonUtils";

    public static boolean isBadJson(String json) {
        return !isGoodJson(json);
    }

    public static boolean isGoodJson(String json) {
        if (json.isEmpty()) {
            return false;
        }
        try {
//            new Gson().fromJson(json, DataBean.class);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

}
