package org.masos.embed.sysconfig.servlet.user;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/users")
public class UserConnection extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            Response.build(resp).json().ok(true);
        }
        Response.build(resp).json().ok(false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User user = new User(username, password);

        if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null
                || user.getPassword().isEmpty()) {
            Response.build(resp).json().ok(false);
        }
        SSHConnection sshConnection = SSHConnection.getDefault(user);
        if (sshConnection.test()) {
            req.getSession().setAttribute("user", user);
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
