package org.masos.embed.sysconfig.servlet.network;

import org.masos.embed.sysconfig.model.executor.Executor;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.SSHExecutor;
import org.masos.embed.sysconfig.model.User;
import org.masos.embed.sysconfig.script.ConnectionScriptManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/networks/status")
public class StatusNetwork extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            Response.build(resp).json().ok(
                    executor.execute(ConnectionScriptManager.WIFI_STATUS));
        }
    }
}
