package group.chon.ide.news.app.accessmethods.httpclient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class HttpRequest {

    private final String url;

    private final HttpMethod method;

    private final List<HttpQueryParameter> queryParameters;

    private final HttpBody body;

}
