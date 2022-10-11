package org.masos.embed.sysconfig.controller.user;

import com.google.gson.Gson;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.dto.DomainStatusDTO;
import org.masos.embed.sysconfig.model.executor.Executor;
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
            String response = executor.execute(ConnectionScriptManager.DDNS_STATUS, false);
            DomainStatusDTO domainStatusDTO = new Gson().fromJson(response, DomainStatusDTO.class);

            if (domainStatusDTO.getDomain().equals("myrobot.bot.chon.group") && domainStatusDTO.getLocalAddress()
                    .equals("192.168.168.1")) {
                Response.build(resp).json().ok(true);
            }

            Response.build(resp).json().ok(false);
        }

    }
}
