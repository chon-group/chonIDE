package group.chon.ide.news.app.accessmethods.httpclient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HttpResponse {

    private final int code;

    private final HttpBody body;

}
