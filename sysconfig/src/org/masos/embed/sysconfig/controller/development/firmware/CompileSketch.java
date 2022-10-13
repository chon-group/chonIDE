package org.masos.embed.sysconfig.controller.development.firmware;

import org.masos.embed.sysconfig.file.content.FirmwareContentManager;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sketchs/compile")
public class CompileSketch extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Executor executor = (Executor) req.getSession().getAttribute("executor");

        if (executor != null) {
            String boardName = req.getParameter("boardName");
            String code = req.getParameter("code");

            String buildSketchPath = FirmwareContentManager.buildSketch(code, executor);

            Response.build(resp).text().ok(
                    executor.execute(FirmwareScriptManager.mountArduinoCompileSketchScript(buildSketchPath, boardName),
                            true));
        }
    }
}
