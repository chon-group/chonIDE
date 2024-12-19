package group.chon.ide.news.app.accessmethods.httpclient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public class HttpQueryParameter {

    private final String name;

    private final Object value;

}
