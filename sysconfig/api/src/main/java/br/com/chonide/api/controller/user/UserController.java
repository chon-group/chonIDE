package br.com.chonide.api.controller.user;

import br.com.chonide.api.authentication.AuthenticatedUser;
import br.com.chonide.api.authentication.SecurityContextHolder;
import br.com.chonide.api.controller.ApiController;
import br.com.chonide.api.controller.ResponseEntity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/users")
public class UserController extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        return ResponseEntity.get().status(HttpServletResponse.SC_OK);
    }

    @Override
    protected ResponseEntity delete(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        SecurityContextHolder.get().getAuthenticatedUsersByToken().remove(authenticatedUser.getToken());
        return ResponseEntity.get().status(HttpServletResponse.SC_OK);
    }
}
