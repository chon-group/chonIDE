package group.chon.ide.api.api.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import group.chon.ide.api.domain.model.Executor;

import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
public class AuthenticatedUser {

    /** Executor de ações no sistema. */
    private final Executor executor;

    /** Token de acesso ao sistema. */
    private final String token;

    /** Data de expiração de acesso ao sistema. */
    private Date expirationDate;

}
