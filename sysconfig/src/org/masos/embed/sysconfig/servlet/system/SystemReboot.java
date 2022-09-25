package org.masos.embed.sysconfig.servlet.system;

import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/system/reboot")
public class SystemReboot extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            req.getSession().invalidate();
            SSHConnection.getDefault(user).execute("reboot");
        }
    }
}
