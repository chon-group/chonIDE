package org.masos.embed.sysconfig.controller.authentication;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class AuthenticatedUser {

    /** Nome do usuário. */
    private final String username;

    /** Senha do usuário. */
    private final String password;

    /** Host do usuário. */
    private final String host;

}
