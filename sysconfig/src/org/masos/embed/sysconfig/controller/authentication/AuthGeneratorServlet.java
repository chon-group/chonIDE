package org.masos.embed.sysconfig.controller.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.masos.embed.sysconfig.controller.ApiController;
import org.masos.embed.sysconfig.model.ResponseEntity;
import org.masos.embed.sysconfig.model.executor.SSHExecutor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@WebServlet("/auth")
public class AuthGeneratorServlet extends ApiController {

    /** Tempo de expiração do usuário autenticado (30 minutos). */
    protected static final long EXPIRATION_TIME = 900000 * 2;

    /** Host padrão. */
    private static final String DEFAULT_HOST = "localhost";

    /** Chave para criptografia pelo algorítmo. */
    private static final String UUID_KEY = "917321c6-78d7-4f78-b0db-1f574ae5d798";

    /** Algorítmo para criação do JWT. */
    private static final Algorithm JWT_ALGORITHM = Algorithm.HMAC256(UUID_KEY);

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, String> parameters) {
        ResponseEntity responseEntity = ResponseEntity.get();
        if (authenticatedUser != null) {
            return responseEntity.status(HttpServletResponse.SC_OK);
        }
        String username = parameters.get("username");
        String password = parameters.get("password");
        String host = parameters.get("host");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).message(
                    "Os parâmetros de 'username' e 'passowrd' estão vazios ou não existem.");
        }
        if (host == null || host.isEmpty()) {
            host = DEFAULT_HOST;
        }

        SSHExecutor sshExecutor = new SSHExecutor(username, password, host);
        if (sshExecutor.test()) {
            String jwt = JWT.create().withSubject(username).sign(JWT_ALGORITHM);
            Date date = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
            AuthenticatedUser newAuthenticatedUser = new AuthenticatedUser(username, password, host);
            newAuthenticatedUser.setExpirationDate(date);
            newAuthenticatedUser.setLastRequisitionDate(date);
            SecurityContextHolder.get().getAuthenticatedUsersByToken().put(jwt, newAuthenticatedUser);
            return responseEntity.status(HttpServletResponse.SC_OK).message("Usuário autenticado.").data(jwt);
        } else {
            return responseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).message(
                    "Não foi possível autenticar seu usuário.");
        }
    }

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, String> parameters) {
        return null;
    }

    @Override
    protected ResponseEntity put(AuthenticatedUser authenticatedUser, Map<String, String> parameters) {
        return null;
    }

    @Override
    protected ResponseEntity delete(AuthenticatedUser authenticatedUser, Map<String, String> parameters) {
        return null;
    }
}
