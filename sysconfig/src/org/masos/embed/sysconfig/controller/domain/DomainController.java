package org.masos.embed.sysconfig.controller.domain;

import org.masos.embed.sysconfig.controller.ApiController;
import org.masos.embed.sysconfig.controller.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.model.ResponseEntity;
import org.masos.embed.sysconfig.script.ConnectionScriptManager;

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
