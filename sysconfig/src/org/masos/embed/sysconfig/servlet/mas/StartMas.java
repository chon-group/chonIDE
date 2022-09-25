package org.masos.embed.sysconfig.servlet.mas;


import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;
import org.masos.embed.sysconfig.script.ReasoningScriptManager;
import org.masos.embed.sysconfig.model.Response;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/mas/start")
public class StartMas extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            Response.build(resp).text().ok(SSHConnection.getDefault(user)
                    .execute(ReasoningScriptManager.EMBEDDED_MAS_START));
        }
    }
}
