package org.masos.embed.sysconfig.api.controller.user;

import org.masos.embed.sysconfig.api.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.api.authentication.SecurityContextHolder;
import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;

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
