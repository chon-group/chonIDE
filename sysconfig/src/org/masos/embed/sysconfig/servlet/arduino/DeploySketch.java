package org.masos.embed.sysconfig.servlet.arduino;


import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;
import org.masos.embed.sysconfig.model.Response;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sketchs/deploy")
public class DeploySketch extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            String boardName = req.getParameter("boardName");
            String serialPort = req.getParameter("serialPort");
            Response.build(resp).text().ok(SSHConnection.getDefault(user)
                    .execute(FirmwareScriptManager.mountArduinoDeploySketchScript(boardName, serialPort)));
        }
    }
}
