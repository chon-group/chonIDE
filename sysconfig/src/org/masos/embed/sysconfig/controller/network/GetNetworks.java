package org.masos.embed.sysconfig.controller.network;

import com.google.gson.*;
import org.masos.embed.sysconfig.controller.JsonManager;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.dto.NetworkResponseDTO;
import org.masos.embed.sysconfig.model.executor.Executor;
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
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            String allNetworksResponse = executor.execute(ConnectionScriptManager.WIFI_SCAN_LIST, false);
            String knownNetworksResponse = executor.execute(ConnectionScriptManager.WIFI_WELL_KNOWN_LIST, false);

            JsonArray allNetworks;
            JsonArray allKnownNetworks;
            Response response = Response.build(resp).json();
            try {
                allNetworks = new Gson().fromJson(allNetworksResponse, JsonArray.class);
                allKnownNetworks = new Gson().fromJson(knownNetworksResponse, JsonArray.class);
            } catch (Exception ignored) {
                response.ok("[]");
                return;
            }

            List<NetworkResponseDTO> networksResponse = new ArrayList<>();
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
                        break;
                    }
                }
                String[] quality = networkJsonElement.get("quality").getAsString().split("/");
                networkJsonElement.remove("quality");
                Double qualityValue;
                try {
                    qualityValue = Double.valueOf(Double.parseDouble(quality[0]) / Double.parseDouble(quality[1]));
                } catch (Exception ignored) {
                    qualityValue = 0.0;
                }
                networkJsonElement.add("quality", new JsonPrimitive(qualityValue));
                String encryption = networkJsonElement.get("encryption").getAsString();
                networkJsonElement.remove("encryption");
                networkJsonElement.add("encryption", new JsonPrimitive(encryption.equals("on")));

                networksResponse.add(JsonManager.get().fromJson(networkJsonElement, NetworkResponseDTO.class));
            }

            Collections.sort(networksResponse);
            response.ok(networksResponse);
        }

    }
}
