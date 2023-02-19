package org.masos.embed.sysconfig.controller.development.mas;

import org.masos.embed.sysconfig.controller.ApiController;
import org.masos.embed.sysconfig.controller.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.model.ResponseEntity;
import org.masos.embed.sysconfig.script.ReasoningScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/mas/stop")
public class MasStop extends ApiController {

    @Override
    protected ResponseEntity put(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String response = authenticatedUser.getExecutor().execute(ReasoningScriptManager.EMBEDDED_MAS_STOP, false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(response);
    }

}
