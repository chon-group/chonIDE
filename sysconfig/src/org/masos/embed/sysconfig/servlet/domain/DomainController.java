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
            Response.build(resp).json().ok(
                    "{ \"domain\":\"robozinho.bot.chon.group\"," + "  \"ddns_user\":\"ddns_user\","
                            + "  \"ddns_token\":\"ddns_token\"," + "  \"localAddress\":\"192.168.168.1 192.168.15.9 "
                            + "2804:7f5:d180:95e1:1435:77bd:36f8:e767\"," + "  \"registredAddress\":\"192.168.15.9\"}");
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
