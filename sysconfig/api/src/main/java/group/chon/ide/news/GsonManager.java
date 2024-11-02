package group.chon.ide.news;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonManager {

    private static GsonManager instance;

    private Gson gson;

    private GsonManager() {
        this.gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    public static Gson get() {
        if (instance == null) {
            instance = new GsonManager();
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
