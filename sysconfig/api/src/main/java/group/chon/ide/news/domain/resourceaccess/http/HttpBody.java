package group.chon.ide.news.domain.resourceaccess.http;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HttpBody {

    private final String content;

    private final String mediaType;

}
