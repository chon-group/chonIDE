package group.chon.ide.api.api.controller.network;

import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.domain.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/networks/ap")
public class ConnectApNetwork extends ApiController {

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String essid = (String) parameters.get("essid");
        String password = (String) parameters.get("password");
        boolean restart = false;
        if (parameters.containsKey("restart")) {
            restart = Boolean.parseBoolean((String) parameters.get("restart"));
        }

        String command = !password.isEmpty() ? ConnectionScriptManager.mountWifiAPModeScript(essid, password) :
                         ConnectionScriptManager.mountWifiAPModaScript(essid);
        if (restart) {
            command += " --reboot";
        }

        authenticatedUser.getExecutor().execute(command, false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK);
    }

}
