package group.chon.ide.news.infra.json;

public interface JsonConverter {

    <T> T extract(String jsonText, Class<T> tClass);

    String serialize(Object obj);

}
