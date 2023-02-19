package org.masos.embed.sysconfig.api.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.masos.embed.sysconfig.domain.model.Executor;

import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
public class AuthenticatedUser {

    /** Executor de ações no sistema. */
    private final Executor executor;

    /** Data de expiração de acesso ao sistema. */
    private Date expirationDate;

    /** Data da última requisição feita. */
    private Date lastRequisitionDate;

}
