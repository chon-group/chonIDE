package group.chon.ide.api.api.controller.user;

import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.authentication.SecurityContextHolder;
import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;

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
