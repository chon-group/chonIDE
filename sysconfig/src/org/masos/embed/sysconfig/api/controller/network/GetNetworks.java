package org.masos.embed.sysconfig.api.controller.network;

import com.google.gson.*;
import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.controller.JsonManager;
import org.masos.embed.sysconfig.api.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;
import org.masos.embed.sysconfig.api.dto.NetworkResponseDTO;
import org.masos.embed.sysconfig.domain.script.ConnectionScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@WebServlet("/api/networks")
public class GetNetworks extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String allNetworksResponse = authenticatedUser.getExecutor().execute(ConnectionScriptManager.WIFI_SCAN_LIST,
                false);
        String knownNetworksResponse = authenticatedUser.getExecutor().execute(
                ConnectionScriptManager.WIFI_WELL_KNOWN_LIST, false);

        JsonArray allNetworks;
        JsonArray allKnownNetworks;
        ResponseEntity responseEntity = ResponseEntity.get().status(HttpServletResponse.SC_OK);
        try {
            allNetworks = new Gson().fromJson(allNetworksResponse, JsonArray.class);
            allKnownNetworks = new Gson().fromJson(knownNetworksResponse, JsonArray.class);
        } catch (Exception ignored) {
            return responseEntity.data(new ArrayList<>());
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
        return responseEntity.data(networksResponse);
    }

}
