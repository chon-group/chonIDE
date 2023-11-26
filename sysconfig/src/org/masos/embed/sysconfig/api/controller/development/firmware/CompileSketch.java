package org.masos.embed.sysconfig.api.controller.development.firmware;

import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.controller.JsonManager;
import org.masos.embed.sysconfig.api.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.domain.file.content.FirmwareContentManager;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;
import org.masos.embed.sysconfig.api.dto.SketchResponseDTO;
import org.masos.embed.sysconfig.domain.model.Executor;
import org.masos.embed.sysconfig.domain.script.FirmwareScriptManager;

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
