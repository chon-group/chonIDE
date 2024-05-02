package br.com.chonide.api.controller.system;

import br.com.chonide.api.authentication.AuthenticatedUser;
import br.com.chonide.api.controller.ApiController;
import br.com.chonide.api.controller.ResponseEntity;
import br.com.chonide.domain.script.SystemScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/system/reboot")
public class SystemReboot extends ApiController {

    @Override
    protected ResponseEntity put(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        authenticatedUser.getExecutor().execute(SystemScriptManager.REBOOT, false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK);
    }

}
