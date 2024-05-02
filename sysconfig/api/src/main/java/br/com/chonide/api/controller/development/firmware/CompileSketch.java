package br.com.chonide.api.controller.development.firmware;

import br.com.chonide.api.controller.ApiController;
import br.com.chonide.api.controller.JsonManager;
import br.com.chonide.api.authentication.AuthenticatedUser;
import br.com.chonide.domain.file.content.FirmwareContentManager;
import br.com.chonide.api.controller.ResponseEntity;
import br.com.chonide.api.dto.SketchResponseDTO;
import br.com.chonide.domain.model.Executor;
import br.com.chonide.domain.script.FirmwareScriptManager;

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
