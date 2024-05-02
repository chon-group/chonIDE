package br.com.chonide.api.controller.development.firmware;

import br.com.chonide.api.controller.ApiController;
import br.com.chonide.api.authentication.AuthenticatedUser;
import br.com.chonide.api.controller.ResponseEntity;
import br.com.chonide.domain.script.FirmwareScriptManager;

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
