package org.masos.embed.sysconfig.servlet.domain;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;
import org.masos.embed.sysconfig.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/domains")
public class DomainController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            Response.build(resp).json().ok(SSHConnection.getDefault(user).execute(ConnectionScriptManager.DDNS_STATUS));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            String domain = req.getParameter("domain");
            SSHConnection.getDefault(user).execute(ConnectionScriptManager.mountDDNSConfScript(domain));
        }
    }
}
