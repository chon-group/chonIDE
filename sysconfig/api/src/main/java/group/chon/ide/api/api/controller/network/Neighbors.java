package group.chon.ide.api.api.controller.network;

import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.domain.model.RuntimeExecutor;
import group.chon.ide.api.domain.script.NeighborsScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/neighbors")
public class Neighbors extends ApiController {

    public Neighbors() {
        super(false);
    }

    @Override
    protected ResponseEntity delete(Map<String, Object> parameters) {
        String execute = new RuntimeExecutor().execute(NeighborsScriptManager.FORGET, false);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK)
                             .data(execute);
    }

    @Override
    protected ResponseEntity get(Map<String, Object> parameters) {
        return ResponseEntity.get().status(HttpServletResponse.SC_OK)
                             .data(new RuntimeExecutor().execute(NeighborsScriptManager.LIST, false));
    }
}
