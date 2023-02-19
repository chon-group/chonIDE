package org.masos.embed.sysconfig.api.controller;

import com.google.gson.Gson;

public class JsonManager {

    private static JsonManager instance;

    private Gson gson;

    private JsonManager() {
        this.gson = new Gson();
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
