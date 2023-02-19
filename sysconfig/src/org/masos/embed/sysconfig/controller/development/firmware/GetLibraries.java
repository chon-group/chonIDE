package org.masos.embed.sysconfig.controller.development.firmware;

import org.masos.embed.sysconfig.controller.ApiController;
import org.masos.embed.sysconfig.controller.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.model.ResponseEntity;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/libraries")
public class GetLibraries extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String librariesResponse = authenticatedUser.getExecutor().execute(FirmwareScriptManager.ARDUINO_LIST_LIBRARIES,
                false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(librariesResponse);
    }

}
