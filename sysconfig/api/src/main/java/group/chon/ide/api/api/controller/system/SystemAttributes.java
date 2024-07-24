package group.chon.ide.api.api.controller.system;

import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;
import lombok.AllArgsConstructor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/system/attributes")
public class SystemAttributes extends ApiController {

    public SystemAttributes() {
        super(false);
    }

    @Override
    protected ResponseEntity get(Map<String, Object> parameters) {
        return ResponseEntity.get().status(HttpServletResponse.SC_OK)
                             .data(new Attributes(System.getProperty("user.name")));
    }

    @AllArgsConstructor
    private static class Attributes {

        /** Usuário do computador. */
        private final String username;

    }

}
