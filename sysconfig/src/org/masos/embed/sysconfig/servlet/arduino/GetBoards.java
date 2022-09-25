package org.masos.embed.sysconfig.servlet.arduino;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;

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
            String boards = executor.execute(FirmwareScriptManager.ARDUINO_LIST_BOARDS);
            Response.build(resp).json().ok(boards);
        }
    }
}
