package org.masos.embed.sysconfig.view;

import org.masos.embed.sysconfig.model.executor.Executor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/manager")
public class ManagerView extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            req.getRequestDispatcher("/manager.html").forward(req, resp);
        } else {
            resp.sendRedirect("/sysconfig/login");
        }
    }
}
