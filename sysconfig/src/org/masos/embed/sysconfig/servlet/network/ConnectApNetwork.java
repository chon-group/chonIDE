package org.masos.embed.sysconfig.servlet.network;

import com.google.gson.Gson;
import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;
import org.masos.embed.sysconfig.model.dto.NetworkWrapper;
import org.masos.embed.sysconfig.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/networks/ap")
public class ConnectApNetwork extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            NetworkWrapper network = new Gson().fromJson(req.getParameter("network"), NetworkWrapper.class);
            String command = !network.getPassword().isEmpty() ? ConnectionScriptManager.mountWifiAPModeScript(
                    network.getEssid(), network.getPassword()) : ConnectionScriptManager.mountWifiAPModaScript(
                    network.getEssid());
            SSHConnection.getDefault(user).execute(command);
        }
    }
}
