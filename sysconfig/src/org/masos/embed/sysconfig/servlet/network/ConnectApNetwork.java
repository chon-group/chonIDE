package org.masos.embed.sysconfig.servlet.network;

import com.google.gson.Gson;
import org.masos.embed.sysconfig.model.dto.NetworkDTO;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/networks/ap")
public class ConnectApNetwork extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            NetworkDTO network = new Gson().fromJson(req.getParameter("network"), NetworkDTO.class);
            String command = !network.getPassword().isEmpty() ? ConnectionScriptManager.mountWifiAPModeScript(
                    network.getEssid(), network.getPassword()) : ConnectionScriptManager.mountWifiAPModaScript(
                    network.getEssid());
            executor.execute(command);
        }
    }
}
