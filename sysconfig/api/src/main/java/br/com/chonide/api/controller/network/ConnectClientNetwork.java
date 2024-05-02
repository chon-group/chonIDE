package br.com.chonide.api.controller.network;

import br.com.chonide.api.authentication.AuthenticatedUser;
import br.com.chonide.api.controller.ApiController;
import br.com.chonide.api.controller.ResponseEntity;
import br.com.chonide.domain.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/networks/client")
public class ConnectClientNetwork extends ApiController {

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String essid = (String) parameters.get("essid");
        String password = (String) parameters.get("password");
        String command = !password.isEmpty() ? ConnectionScriptManager.mountWifiClientModeScript(essid, password) :
                         ConnectionScriptManager.mountWifiClientModeScript(essid);
        authenticatedUser.getExecutor().execute(command, false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK);
    }
}
