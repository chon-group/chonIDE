package br.com.chonide.api.controller.development.firmware;

import br.com.chonide.api.controller.ApiController;
import br.com.chonide.api.authentication.AuthenticatedUser;
import br.com.chonide.api.controller.ResponseEntity;
import br.com.chonide.domain.script.FirmwareScriptManager;

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
