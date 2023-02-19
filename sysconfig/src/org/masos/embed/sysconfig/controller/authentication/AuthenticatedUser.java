package org.masos.embed.sysconfig.controller.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
public class AuthenticatedUser {

    /** Nome do usuário. */
    private final String username;

    /** Senha do usuário. */
    private final String password;

    /** Host do usuário. */
    private final String host;

    /** Data de expiração de acesso ao sistema. */
    private Date expirationDate;

    /** Data da última requisição feita. */
    private Date lastRequisitionDate;

}
