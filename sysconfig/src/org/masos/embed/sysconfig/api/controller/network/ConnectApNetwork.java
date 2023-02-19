package org.masos.embed.sysconfig.api.controller.network;

import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.controller.JsonManager;
import org.masos.embed.sysconfig.api.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;
import org.masos.embed.sysconfig.api.dto.NetworkRequestDTO;
import org.masos.embed.sysconfig.domain.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/networks/ap")
public class ConnectApNetwork extends ApiController {

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        NetworkRequestDTO network = JsonManager.get().fromJson(parameters.get("network").toString(),
                NetworkRequestDTO.class);
        String command = !network.getPassword().isEmpty() ? ConnectionScriptManager.mountWifiAPModeScript(
                network.getEssid(), network.getPassword()) : ConnectionScriptManager.mountWifiAPModaScript(
                network.getEssid());
        authenticatedUser.getExecutor().execute(command, false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK);
    }

}
