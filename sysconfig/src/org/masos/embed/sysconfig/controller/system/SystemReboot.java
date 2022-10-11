package org.masos.embed.sysconfig.controller.system;

import org.masos.embed.sysconfig.model.executor.Executor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/system/reboot")
public class SystemReboot extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            req.getSession().invalidate();
            executor.execute("reboot", false);
        }
    }
}
