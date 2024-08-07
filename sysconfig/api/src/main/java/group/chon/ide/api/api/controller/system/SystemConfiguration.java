package group.chon.ide.api.api.controller.system;

import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.domain.model.RuntimeExecutor;
import group.chon.ide.api.domain.script.SystemScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/system/configuration")
public class SystemConfiguration extends ApiController {

    public SystemConfiguration() {
        super(false);
    }

    @Override
    protected ResponseEntity get(Map<String, Object> parameters) {
        return ResponseEntity.get().status(HttpServletResponse.SC_OK)
                             .data(new RuntimeExecutor().execute(SystemScriptManager.GET_CONFIGURATION, false));
    }
}
