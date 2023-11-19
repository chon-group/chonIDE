package org.masos.embed.sysconfig.api.controller.development.firmware;

import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.domain.file.content.FirmwareContentManager;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;
import org.masos.embed.sysconfig.domain.model.Executor;
import org.masos.embed.sysconfig.domain.script.FirmwareScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/sketchs/compile")
public class CompileSketch extends ApiController {

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String boardName = parameters.get("boardName").toString();
        String code = parameters.get("code").toString();
    
        Executor executor = authenticatedUser.getExecutor();
        String buildSketchPath = FirmwareContentManager.buildSketch(code, executor);

        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(
                executor.execute(FirmwareScriptManager.mountArduinoCompileSketchScript(buildSketchPath, boardName),
                        true));
    }

}
