package org.masos.embed.sysconfig.servlet.user;

import com.google.gson.Gson;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.model.response.DomainStatus;
import org.masos.embed.sysconfig.script.ConnectionScriptManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users/first-access")
public class UserFirstAccess extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            String response = executor.execute(ConnectionScriptManager.DDNS_STATUS);
            DomainStatus domainStatus = new Gson().fromJson(response, DomainStatus.class);

            if (domainStatus.getDomain().equals("myrobot.bot.chon.group") && domainStatus.getLocalAddress().equals(
                    "192.168.168.1")) {
                Response.build(resp).json().ok(true);
            }

            Response.build(resp).json().ok(false);
        }

    }
}
