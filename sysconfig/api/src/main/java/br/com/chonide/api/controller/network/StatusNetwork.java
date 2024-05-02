package br.com.chonide.api.controller.network;

import br.com.chonide.api.controller.ApiController;
import br.com.chonide.api.authentication.AuthenticatedUser;
import br.com.chonide.api.controller.ResponseEntity;
import br.com.chonide.domain.script.ConnectionScriptManager;

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
