package group.chon.ide.api.api.controller.user;

import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.JsonManager;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.api.dto.DomainStatusResponseDTO;
import group.chon.ide.api.domain.script.ConnectionScriptManager;

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
