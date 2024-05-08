package group.chon.ide.api.api.controller.development.firmware;

import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.JsonManager;
import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.domain.file.content.FirmwareContentManager;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.api.dto.SketchResponseDTO;
import group.chon.ide.api.domain.model.Executor;
import group.chon.ide.api.domain.script.FirmwareScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/sketchs/compile")
public class CompileSketch extends ApiController {

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        SketchResponseDTO sketch = JsonManager.get().fromJson(parameters.get("data").toString(), SketchResponseDTO.class);
   
        Executor executor = authenticatedUser.getExecutor();
        String buildSketchPath = FirmwareContentManager.buildSketch(sketch.getcode(), executor);

        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(
                executor.execute(FirmwareScriptManager.mountArduinoCompileSketchScript(buildSketchPath, sketch.getBoardName()),
                        true));
    }

}
