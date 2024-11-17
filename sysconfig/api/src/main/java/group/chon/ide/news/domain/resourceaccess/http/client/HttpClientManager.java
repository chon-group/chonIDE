package group.chon.ide.news.domain.resourceaccess.http.client;

import lombok.Getter;


public class HttpClientManager {

    @Getter
    private static HttpClientManager instance = new HttpClientManager();

    @Getter
    private final HttpClient client;

    private HttpClientManager() {
        this.client = new StandardHttpClient();
    }
}
