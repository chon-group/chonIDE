package group.chon.ide.api.api.controller.development.firmware;

import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.domain.script.FirmwareScriptManager;

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
