package group.chon.ide.api.api.controller.development.firmware;

import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.domain.script.FirmwareScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/boards")
public class GetBoards extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String boardsResponse = authenticatedUser.getExecutor().execute(FirmwareScriptManager.ARDUINO_LIST_BOARDS,
                false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(boardsResponse);
    }

}
