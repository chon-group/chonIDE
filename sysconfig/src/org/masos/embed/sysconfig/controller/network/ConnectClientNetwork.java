package org.masos.embed.sysconfig.controller.network;

import org.masos.embed.sysconfig.controller.ControllerUtils;
import org.masos.embed.sysconfig.controller.JsonManager;
import org.masos.embed.sysconfig.model.dto.NetworkRequestDTO;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/networks/client")
public class ConnectClientNetwork extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            NetworkRequestDTO network = JsonManager.get().fromJson(ControllerUtils.getRequestBody(req),
                    NetworkRequestDTO.class);
            String command = !network.getPassword().isEmpty() ? ConnectionScriptManager.mountWifiClientModeScript(
                    network.getEssid(), network.getPassword()) : ConnectionScriptManager.mountWifiClientModeScript(
                    network.getEssid());
            executor.execute(command, false);
        }
    }
}
