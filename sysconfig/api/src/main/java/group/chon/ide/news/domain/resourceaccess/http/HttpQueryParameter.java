package group.chon.ide.news.domain.resourceaccess.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public class HttpQueryParameter {

    private final String name;

    private final Object value;

}
