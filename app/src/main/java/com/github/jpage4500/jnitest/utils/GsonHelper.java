package com.github.jpage4500.jnitest.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {

    private static Gson singleton = null;

    public static Gson getInstance() {
        if (singleton == null) {
            Gson gson = new GsonBuilder().create();
            GsonHelper.singleton = gson;
        }
        return singleton;
    }

}
