package br.com.chonide.api.controller.domain;

import br.com.chonide.api.controller.ApiController;
import br.com.chonide.api.authentication.AuthenticatedUser;
import br.com.chonide.api.controller.ResponseEntity;
import br.com.chonide.domain.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/domains")
public class DomainController extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(
                authenticatedUser.getExecutor().execute(ConnectionScriptManager.DDNS_STATUS, false));
    }

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String domain = parameters.get("domain").toString();
        String domainCommand = ConnectionScriptManager.mountDDNSConfScript(domain);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(
                authenticatedUser.getExecutor().execute(domainCommand, false));
    }

}
