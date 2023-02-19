package org.masos.embed.sysconfig.controller.system;

import org.masos.embed.sysconfig.controller.ApiController;
import org.masos.embed.sysconfig.controller.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.model.ResponseEntity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/system/poweroff")
public class SystemPowerOff extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        return null;
    }

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        return null;
    }

    @Override
    protected ResponseEntity put(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        authenticatedUser.getExecutor().execute("poweroff", false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK);
    }

    @Override
    protected ResponseEntity delete(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        return null;
    }
}
