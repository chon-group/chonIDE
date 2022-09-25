package org.masos.embed.sysconfig.servlet.network;

import com.google.gson.*;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;
import org.masos.embed.sysconfig.model.response.NetworkResponse;
import org.masos.embed.sysconfig.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/networks")
public class GetNetworks extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            SSHConnection sshConnection = SSHConnection.getDefault(user);

            String allNetworksResponse = sshConnection.execute(ConnectionScriptManager.WIFI_SCAN_LIST);
            String knownNetworksResponse = sshConnection.execute(ConnectionScriptManager.WIFI_WELL_KNOWN_LIST);

            JsonArray allNetworks;
            JsonArray allKnownNetworks;
            try {
                allNetworks = new Gson().fromJson(allNetworksResponse, JsonArray.class);
                allKnownNetworks = new Gson().fromJson(knownNetworksResponse, JsonArray.class);
            } catch (Exception ignored) {
                Response.build(resp).json().ok("[]");
                return;
            }

            List<NetworkResponse> networksResponse = new ArrayList<>();
            for (JsonElement network : allNetworks) {
                JsonObject networkJsonElement = network.getAsJsonObject();
                if (networkJsonElement.get("essid").getAsString().isEmpty()) {
                    continue;
                }
                for (JsonElement knowNetwork : allKnownNetworks) {
                    if (knowNetwork.getAsJsonObject().get("essid").getAsString().equals(
                            networkJsonElement.get("essid").getAsString())) {
                        networkJsonElement.add("password",
                                new JsonPrimitive(knowNetwork.getAsJsonObject().get("pass").getAsString()));
                    }
                }
                String[] quality = networkJsonElement.get("quality").getAsString().split("/");
                networkJsonElement.remove("quality");
                networkJsonElement.add("quality", new JsonPrimitive(
                        Double.valueOf(Double.parseDouble(quality[0]) / Double.parseDouble(quality[1]))));
                String encryption = networkJsonElement.get("encryption").getAsString();
                networkJsonElement.remove("encryption");
                networkJsonElement.add("encryption", new JsonPrimitive(encryption.equals("on") ? true : false));

                networksResponse.add(new Gson().fromJson(networkJsonElement, NetworkResponse.class));
            }

            Collections.sort(networksResponse);
            Response.build(resp).json().ok(new Gson().toJson(networksResponse));
        }

    }
}
