package group.chon.ide.news.infra.json;

import lombok.Getter;

public class JsonConverterProvider {

    @Getter
    private static JsonConverterProvider instance = new JsonConverterProvider();

    @Getter
    private final JsonConverter converter;

    private JsonConverterProvider() {
        this.converter = new StandardJsonConverter();
    }

}
