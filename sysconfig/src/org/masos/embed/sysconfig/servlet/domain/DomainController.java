package org.masos.embed.sysconfig.servlet.domain;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/domains")
public class DomainController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            Response.build(resp).json().ok(executor.execute(ConnectionScriptManager.DDNS_STATUS));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            String domain = req.getParameter("domain");
            executor.execute(ConnectionScriptManager.mountDDNSConfScript(domain));
        }
    }
}
