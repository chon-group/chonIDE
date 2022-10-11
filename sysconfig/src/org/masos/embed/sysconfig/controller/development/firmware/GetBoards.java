package org.masos.embed.sysconfig.controller.development.firmware;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;
import org.masos.embed.sysconfig.controller.ControllerUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/boards")
public class GetBoards extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            String boards = (String) req.getSession().getAttribute("boards");
            if (boards == null || ControllerUtils.isRefreshRequest(req)) {
                boards = executor.execute(FirmwareScriptManager.ARDUINO_LIST_BOARDS);
                req.getSession().setAttribute("boards", boards);
            }
            Response.build(resp).json().ok(boards);
        }
    }
}
