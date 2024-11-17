package group.chon.ide.news.domain.service.mindapi;

import group.chon.ide.news.domain.resourceaccess.http.HttpMethod;
import group.chon.ide.news.domain.resourceaccess.http.HttpQueryParameter;
import group.chon.ide.news.domain.resourceaccess.http.RequestInfo;
import group.chon.ide.news.domain.resourceaccess.http.client.HttpClient;
import group.chon.ide.news.domain.resourceaccess.http.client.HttpClientManager;

import java.util.Arrays;
import java.util.List;

public class MindAPIService {

    private static final String MIND_API_URL_PATTERN = "http://%s:3275/agents";

    private String url;

    private final HttpClient httpClient;

    public MindAPIService(String host) {
        this.httpClient = HttpClientManager.getInstance().getClient();
    }

    public void bindHost(String host) {
        this.url = String.format(MIND_API_URL_PATTERN, host);
    }

    public Object getAgentState(String name, int cycle) {
        List<HttpQueryParameter> parameters = Arrays.asList(new HttpQueryParameter("name", name), new HttpQueryParameter("cycle", cycle));
        return this.httpClient.request(new RequestInfo(this.url, HttpMethod.GET, parameters, null));
    }

    public Object getAllAgentStates() {
        return this.httpClient.request(new RequestInfo(this.url, HttpMethod.GET, null, null));
    }
}
