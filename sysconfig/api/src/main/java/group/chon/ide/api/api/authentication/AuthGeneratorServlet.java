package group.chon.ide.api.api.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.domain.model.Executor;
import group.chon.ide.api.domain.model.RuntimeExecutor;
import group.chon.ide.api.domain.model.SSHExecutor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@WebServlet("/auth")
public class AuthGeneratorServlet extends ApiController {


    /** Tempo de expiração do usuário autenticado (30 minutos). */
    protected static final long EXPIRATION_TIME = 900000 * 4;

    /** Host padrão. */
    private static final String DEFAULT_HOST = "localhost";

    /** Chave para criptografia pelo algorítmo. */
    private static final String UUID_KEY = "917321c6-78d7-4f78-b0db-1f574ae5d798";

    /** Algorítmo para criação do JWT. */
    private static final Algorithm JWT_ALGORITHM = Algorithm.HMAC256(UUID_KEY);

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        ResponseEntity responseEntity = ResponseEntity.get();
        if (authenticatedUser != null) {
            return responseEntity.status(HttpServletResponse.SC_OK);
        }
        String username = (String) parameters.get("username");
        String password = (String) parameters.get("password");
        String host = (String) parameters.get("host");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).message(
                    "The 'username' and 'password' parameters are empty or do not exist.");
        }

        if (host == null || host.isEmpty()) {
            host = DEFAULT_HOST;
        }

        SSHExecutor sshExecutor = new SSHExecutor(username, password, host);
        if (sshExecutor.test()) {
            String jwt = JWT.create().withSubject(username).sign(JWT_ALGORITHM);
            Date date = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
            Executor executor;
            if (host.equals(DEFAULT_HOST)) {
                executor = new RuntimeExecutor();
            } else {
                executor = new SSHExecutor(username, password, host);
            }
            AuthenticatedUser newAuthenticatedUser = new AuthenticatedUser(executor, jwt);
            newAuthenticatedUser.setExpirationDate(date);
            SecurityContextHolder.get().getAuthenticatedUsersByToken().put(jwt, newAuthenticatedUser);
            return responseEntity.status(HttpServletResponse.SC_OK).message("Authenticated user").data(jwt);
        } else {
            return responseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).message(
                    "Unable to authenticate your user");
        }
    }
}
