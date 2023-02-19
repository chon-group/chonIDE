package org.masos.embed.sysconfig.api.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;
import org.masos.embed.sysconfig.domain.model.Executor;
import org.masos.embed.sysconfig.domain.model.RuntimeExecutor;
import org.masos.embed.sysconfig.domain.model.SSHExecutor;

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
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        ResponseEntity responseEntity = ResponseEntity.get();
        if (authenticatedUser != null) {
            return responseEntity.status(HttpServletResponse.SC_OK);
        }
        String username = (String) parameters.get("username");
        String password = (String) parameters.get("password");
        String host = (String) parameters.get("host");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).message(
                    "Os parâmetros de 'username' e 'password' estão vazios ou não existem.");
        }
        boolean isDefaultHost = false;
        if (host == null || host.isEmpty()) {
            host = DEFAULT_HOST;
            isDefaultHost = true;
        }

        SSHExecutor sshExecutor = new SSHExecutor(username, password, host);
        if (sshExecutor.test()) {
            String jwt = JWT.create().withSubject(username).sign(JWT_ALGORITHM);
            Date date = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
            Executor executor;
            if (isDefaultHost) {
                executor = new RuntimeExecutor();
            } else {
                executor = new SSHExecutor(username, password, host);
            }
            AuthenticatedUser newAuthenticatedUser = new AuthenticatedUser(executor);
            newAuthenticatedUser.setExpirationDate(date);
            newAuthenticatedUser.setLastRequisitionDate(date);
            SecurityContextHolder.get().getAuthenticatedUsersByToken().put(jwt, newAuthenticatedUser);
            return responseEntity.status(HttpServletResponse.SC_OK).message("Usuário autenticado.").data(jwt);
        } else {
            return responseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).message(
                    "Não foi possível autenticar seu usuário.");
        }
    }
}
