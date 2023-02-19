package org.masos.embed.sysconfig.controller.development.firmware;

import org.masos.embed.sysconfig.controller.ApiController;
import org.masos.embed.sysconfig.controller.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.model.ResponseEntity;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;

import javax.servlet.annotation.WebServlet;
import java.util.Map;

@WebServlet("/api/sketchs/deploy")
public class DeploySketch extends ApiController {

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String boardName = parameters.get("boardName").toString();
        String serialPort = parameters.get("serialPort").toString();
        return ResponseEntity.get().data(authenticatedUser.getExecutor()
                .execute(FirmwareScriptManager.mountArduinoDeploySketchScript(boardName, serialPort), true));
    }

}
