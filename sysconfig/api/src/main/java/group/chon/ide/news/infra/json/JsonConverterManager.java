package group.chon.ide.news.infra.json;

import lombok.Getter;

public class JsonConverterManager {

    @Getter
    private static JsonConverterManager instance = new JsonConverterManager();

    @Getter
    private final JsonConverter converter;

    private JsonConverterManager() {
        this.converter = new GsonJsonConverter();
    }

}
