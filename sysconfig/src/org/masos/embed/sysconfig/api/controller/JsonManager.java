package org.masos.embed.sysconfig.api.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonManager {

    private static JsonManager instance;

    private Gson gson;

    private JsonManager() {
        this.gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    public static Gson get() {
        if (instance == null) {
            instance = new JsonManager();
        }
        return instance.getGson();
    }

    /**
     * @return {@link #gson}
     */
    public Gson getGson() {
        return this.gson;
    }
}
