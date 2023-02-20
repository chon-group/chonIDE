package org.masos.embed.sysconfig.api.controller.user;

import org.masos.embed.sysconfig.api.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.controller.JsonManager;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;
import org.masos.embed.sysconfig.api.dto.DomainStatusResponseDTO;
import org.masos.embed.sysconfig.domain.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/users/first-access")
public class UserFirstAccess extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String response = authenticatedUser.getExecutor().execute(ConnectionScriptManager.DDNS_STATUS, false);
        DomainStatusResponseDTO domainStatusResponseDTO = JsonManager.get().fromJson(response, DomainStatusResponseDTO.class);

        ResponseEntity responseEntity = ResponseEntity.get().status(HttpServletResponse.SC_OK);
        if (domainStatusResponseDTO.getDomain().equals("myrobot.bot.chon.group") && domainStatusResponseDTO.getLocalAddress().equals(
                "192.168.168.1")) {
            return responseEntity.data(true);
        }

        return responseEntity.data(false);
    }

}
