package com.pjwxw.sayeng.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GsonUtil {

    public static Gson gson = new Gson();

    public static <T> T  parseGson(String jsonStr){
        return gson.fromJson(jsonStr,new TypeToken<T>(){}.getType());
    }
}
