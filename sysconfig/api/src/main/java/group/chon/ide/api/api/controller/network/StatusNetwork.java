package group.chon.ide.api.api.controller.network;

import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.domain.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/networks/status")
public class StatusNetwork extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(
                authenticatedUser.getExecutor().execute(ConnectionScriptManager.WIFI_STATUS, false)
                        .replace(" Nickname", ""));
    }

}
