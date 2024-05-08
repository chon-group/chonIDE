package group.chon.ide.api.api.controller.network;

import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.domain.script.ConnectionScriptManager;

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
