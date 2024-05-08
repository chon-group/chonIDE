package group.chon.ide.api.api.controller.system;

import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.domain.script.SystemScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/system/reboot")
public class SystemReboot extends ApiController {

    @Override
    protected ResponseEntity put(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        authenticatedUser.getExecutor().execute(SystemScriptManager.REBOOT, false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK);
    }

}
