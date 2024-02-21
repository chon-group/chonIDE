package org.masos.embed.sysconfig.api.controller.development.firmware;

import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;
import org.masos.embed.sysconfig.domain.script.FirmwareScriptManager;

import javax.servlet.annotation.WebServlet;

import java.util.Map;

@WebServlet("/api/sketchs/deploy")
public class DeploySketch extends ApiController {

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String boardName = parameters.get("boardName").toString();
        String serialPort = parameters.get("serialPort").toString();
        return ResponseEntity.get().status(200).data(authenticatedUser.getExecutor()
                .execute(FirmwareScriptManager.mountArduinoDeploySketchScript(boardName, serialPort), true));
    }

}
