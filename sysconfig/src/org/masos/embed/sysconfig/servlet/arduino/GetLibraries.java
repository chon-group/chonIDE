package org.masos.embed.sysconfig.servlet.arduino;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/libraries")
public class GetLibraries extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            Response.build(resp).json().ok(executor.execute(FirmwareScriptManager.ARDUINO_LIST_LIBRARIES));
        }
    }
}
