package org.masos.embed.sysconfig.servlet.user;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.User;
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

    private static final String DEFAULT_HOST = "locahost";

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

        User user = new User(username, password);

        if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null
                || user.getPassword().isEmpty()) {
            Response.build(resp).json().ok(false);
        }

        SSHExecutor sshExecutor = new SSHExecutor(user.getUsername(), user.getPassword(), host);
        if (sshExecutor.test()) {
            Executor executor;
            if (host.equals(DEFAULT_HOST)) {
                executor = new RuntimeExecutor();
            } else {
                executor = sshExecutor;
            }
            req.getSession().setAttribute("executor", executor);
            Response.build(resp).json().ok(true);
        }

        Response.build(resp).json().ok(false);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        if (session != null) {
            session.invalidate();
        }
    }
}
