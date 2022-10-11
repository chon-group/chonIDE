package org.masos.embed.sysconfig.controller.development.firmware;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;
import org.masos.embed.sysconfig.controller.ControllerUtils;

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
            String libraries = (String) req.getSession().getAttribute("libraries");
            if (libraries == null || ControllerUtils.isRefreshRequest(req)) {
                libraries = executor.execute(FirmwareScriptManager.ARDUINO_LIST_LIBRARIES, false);
                req.getSession().setAttribute("libraries", libraries);
            }
            Response.build(resp).json().ok(libraries);
        }
    }
}
