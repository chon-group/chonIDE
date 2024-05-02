package br.com.chonide.api.controller.system;

import br.com.chonide.api.authentication.AuthenticatedUser;
import br.com.chonide.api.controller.ApiController;
import br.com.chonide.api.controller.ResponseEntity;
import br.com.chonide.domain.script.SystemScriptManager;

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
