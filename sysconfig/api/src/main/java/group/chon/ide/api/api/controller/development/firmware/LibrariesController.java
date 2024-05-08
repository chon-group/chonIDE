package group.chon.ide.api.api.controller.development.firmware;

import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.domain.file.content.FirmwareContentManager;
import group.chon.ide.api.domain.model.SSHExecutor;
import group.chon.ide.api.domain.script.FirmwareScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/libraries")
public class LibrariesController extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String librariesResponse = authenticatedUser.getExecutor().execute(FirmwareScriptManager.ARDUINO_LIST_LIBRARIES,
                false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(librariesResponse);
    }

    @Override
    protected ResponseEntity delete(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        if (authenticatedUser.getExecutor() instanceof SSHExecutor) {
            return ResponseEntity.get().status(HttpServletResponse.SC_BAD_REQUEST);
        }
        String libName = (String) parameters.get("name");
        if (libName == null || libName.isEmpty() || libName.equals("Javino")) {
            return ResponseEntity.get().status(HttpServletResponse.SC_BAD_REQUEST);
        }
        FirmwareContentManager.deleteLibrary(libName);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK);
    }
}
