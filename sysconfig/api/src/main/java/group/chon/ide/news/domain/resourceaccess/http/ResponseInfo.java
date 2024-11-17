package group.chon.ide.news.domain.resourceaccess.http;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResponseInfo {

    private final int code;

    private final HttpBody HTTPBody;

}
