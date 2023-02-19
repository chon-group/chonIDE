package org.masos.embed.sysconfig.controller.user;

import com.google.gson.Gson;
import org.masos.embed.sysconfig.controller.ApiController;
import org.masos.embed.sysconfig.controller.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.model.ResponseEntity;
import org.masos.embed.sysconfig.model.dto.DomainStatusDTO;
import org.masos.embed.sysconfig.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/users/first-access")
public class UserFirstAccess extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String response = authenticatedUser.getExecutor().execute(ConnectionScriptManager.DDNS_STATUS, false);
        DomainStatusDTO domainStatusDTO = new Gson().fromJson(response, DomainStatusDTO.class);

        ResponseEntity responseEntity = ResponseEntity.get().status(HttpServletResponse.SC_OK);
        if (domainStatusDTO.getDomain().equals("myrobot.bot.chon.group") && domainStatusDTO.getLocalAddress().equals(
                "192.168.168.1")) {
            return responseEntity.data(true);
        }

        return responseEntity.data(false);
    }

}
