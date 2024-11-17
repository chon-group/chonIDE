package group.chon.ide.news.domain.resourceaccess.http.client;

import group.chon.ide.news.domain.resourceaccess.http.RequestInfo;
import group.chon.ide.news.domain.resourceaccess.http.ResponseInfo;

public interface HttpClient {

    ResponseInfo request(RequestInfo requestInfo);

}
