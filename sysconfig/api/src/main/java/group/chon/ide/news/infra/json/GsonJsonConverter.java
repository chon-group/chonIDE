package group.chon.ide.news.infra.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonJsonConverter implements JsonConverter {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    protected GsonJsonConverter() {

    }

    @Override
    public <T> T extract(String jsonText, Class<T> tClass) {
        return GSON.fromJson(jsonText, tClass);
    }

    @Override
    public String serialize(Object obj) {
        return GSON.toJson(obj);
    }
}
