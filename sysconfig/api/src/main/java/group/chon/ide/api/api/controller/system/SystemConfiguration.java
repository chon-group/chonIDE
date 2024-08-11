package group.chon.ide.api.api.controller.system;

import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.domain.script.SystemScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/system/configuration")
public class SystemConfiguration extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String execute = authenticatedUser.getExecutor()
                                          .execute(SystemScriptManager.GET_CONFIGURATION, false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK)
                             .data(execute);
    }
}
