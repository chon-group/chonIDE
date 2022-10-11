package org.masos.embed.sysconfig.controller.user;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.model.executor.RuntimeExecutor;
import org.masos.embed.sysconfig.model.executor.SSHExecutor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/users")
public class UserConnection extends HttpServlet {

    private static final String DEFAULT_HOST = "localhost";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            Response.build(resp).json().ok(true);
        }
        Response.build(resp).json().ok(false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String host = req.getParameter("host");

        if (host == null || host.isEmpty()) {
            host = DEFAULT_HOST;
        }

        Response response = Response.build(resp);

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            response.ok(false);
        }

        SSHExecutor sshExecutor = new SSHExecutor(username, password, host);
        if (sshExecutor.test()) {
            Executor executor;
            if (host.equals(DEFAULT_HOST)) {
                executor = new RuntimeExecutor();
            } else {
                executor = sshExecutor;
            }
            req.getSession().setAttribute("executor", executor);
            response.ok(true);
            return;
        }

        response.ok(false);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        if(session != null) {
            session.invalidate();
        }
    }
}
