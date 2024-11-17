package group.chon.ide.news.domain.resourceaccess.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class RequestInfo {

    private final String url;

    private final HttpMethod method;

    private final List<HttpQueryParameter> queryParameters;

    private final HttpBody body;

}
