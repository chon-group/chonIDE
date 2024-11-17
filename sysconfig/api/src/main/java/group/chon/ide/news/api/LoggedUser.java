package group.chon.ide.news.api;

import group.chon.ide.news.domain.devcontext.DevelopmentContext;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
public class LoggedUser {

    private final String token;

    private final DevelopmentContext developmentContext;

    private Date expirationDate;

}
