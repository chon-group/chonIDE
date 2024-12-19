package group.chon.ide.news.app.service.mindapi;

import group.chon.ide.news.app.accessmethods.httpclient.HttpClient;
import group.chon.ide.news.app.accessmethods.httpclient.HttpMethod;
import group.chon.ide.news.app.accessmethods.httpclient.HttpQueryParameter;
import group.chon.ide.news.app.accessmethods.httpclient.HttpRequest;
import group.chon.ide.news.infra.json.JsonConverter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class MindAPIService {

    private static final String MIND_API_URL_PATTERN = "http://%s:3275/agents";
    private final HttpClient httpClient;
    private final JsonConverter jsonConverter;
    private String url;

    public void bindHost(String host) {
        this.url = String.format(MIND_API_URL_PATTERN, host);
    }

    public Object getAgentState(String name, int cycle) {
        List<HttpQueryParameter> parameters = Arrays.asList(new HttpQueryParameter("name", name), new HttpQueryParameter("cycle", cycle));
        return this.httpClient.request(new HttpRequest(this.url, HttpMethod.GET, parameters, null));
    }

    public Object getAllAgentStates() {
        return this.httpClient.request(new HttpRequest(this.url, HttpMethod.GET, null, null));
    }
}
