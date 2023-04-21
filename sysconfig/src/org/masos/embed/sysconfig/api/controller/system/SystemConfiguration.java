package org.masos.embed.sysconfig.api.controller.system;

import org.masos.embed.sysconfig.api.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;
import org.masos.embed.sysconfig.domain.script.SystemScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/system/configuration")
public class SystemConfiguration extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String response = authenticatedUser.getExecutor().execute(SystemScriptManager.GET_CONFIGURATION, false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(response);
    }
}
