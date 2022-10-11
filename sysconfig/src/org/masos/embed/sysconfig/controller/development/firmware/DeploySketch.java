package org.masos.embed.sysconfig.controller.development.firmware;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sketchs/deploy")
public class DeploySketch extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            String boardName = req.getParameter("boardName");
            String serialPort = req.getParameter("serialPort");
            Response.build(resp).text().ok(
                    executor.execute(FirmwareScriptManager.mountArduinoDeploySketchScript(boardName, serialPort)));
        }
    }
}
