package org.masos.embed.sysconfig.controller.domain;

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
            String domain = (String) req.getSession().getAttribute("domain");
            if (domain == null) {
                domain = executor.execute(ConnectionScriptManager.DDNS_STATUS);
                req.getSession().setAttribute("domain", domain);
            }
            Response.build(resp).json().ok(domain);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            String domain = req.getParameter("domain");
            String domainCommand = ConnectionScriptManager.mountDDNSConfScript(domain);
            req.getSession().setAttribute("domain", executor.execute(domainCommand));
        }
    }
}
