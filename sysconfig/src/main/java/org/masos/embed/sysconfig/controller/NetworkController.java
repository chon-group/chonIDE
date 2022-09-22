package org.masos.embed.sysconfig.controller;

import com.google.gson.*;
import org.masos.embed.sysconfig.SessionManager;
import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.dto.NetworkWrapper;
import org.masos.embed.sysconfig.model.response.NetworkResponse;
import org.masos.embed.sysconfig.script.ConnectionScriptManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@SessionAttributes("sshConnection")
public class NetworkController {

    @PostMapping("/network/domain")
    public String registryDomain(@RequestParam String domain) {
        return SSHConnection.getDefault(SessionManager.getUser()).execute(
                ConnectionScriptManager.mountDDNSConfScript(domain));
    }

    @GetMapping("/network/domain/status")
    public String domainStatus() {
        return "{ \"domain\":\"robozinho.bot.chon.group\"," + "  \"ddns_user\":\"ddns_user\","
                + "  \"ddns_token\":\"ddns_token\","
                + "  \"localAddress\":\"192.168.168.1 192.168.15.9 2804:7f5:d180:95e1:1435:77bd:36f8:e767\","
                + "  \"registredAddress\":\"192.168.15.9\"}";
    }

    @PostMapping("/network/connect/client")
    public String connectClientMode(@RequestBody NetworkWrapper network) {
        String command = !network.getPassword().isEmpty() ? ConnectionScriptManager.mountWifiClientModeScript(
                network.getEssid(), network.getPassword()) : ConnectionScriptManager.mountWifiClientModeScript(
                network.getEssid());
        return SSHConnection.getDefault(SessionManager.getUser()).execute(command);
    }

    @PostMapping("/network/connect/ap")
    public String connectApMode(@RequestBody NetworkWrapper network) {
        String command = !network.getPassword().isEmpty() ? ConnectionScriptManager.mountWifiAPModeScript(
                network.getEssid(), network.getPassword()) : ConnectionScriptManager.mountWifiAPModaScript(
                network.getEssid());
        return SSHConnection.getDefault(SessionManager.getUser()).execute(command);
    }

    @GetMapping("/network/status")
    public String status() {
        return SSHConnection.getDefault(SessionManager.getUser()).execute(ConnectionScriptManager.WIFI_STATUS);
    }

    @GetMapping("/networks")
    public List<NetworkResponse> getAll() {
        SSHConnection sshConnection = SSHConnection.getDefault(SessionManager.getUser());
        String allNetworksResponse = sshConnection.execute(ConnectionScriptManager.WIFI_SCAN_LIST);
        String knownNetworksResponse = sshConnection.execute(ConnectionScriptManager.WIFI_WELL_KNOWN_LIST);

        JsonArray allNetworks;
        JsonArray allKnownNetworks;
        try {
            allNetworks = new Gson().fromJson(allNetworksResponse, JsonArray.class);
            allKnownNetworks = new Gson().fromJson(knownNetworksResponse, JsonArray.class);
        } catch (Exception ignored) {
            return new ArrayList<>();
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
            networkJsonElement.add("quality",
                    new JsonPrimitive(Double.valueOf(Double.parseDouble(quality[0]) / Double.parseDouble(quality[1]))));
            String encryption = networkJsonElement.get("encryption").getAsString();
            networkJsonElement.remove("encryption");
            networkJsonElement.add("encryption", new JsonPrimitive(encryption.equals("on") ? true : false));

            networksResponse.add(new Gson().fromJson(networkJsonElement, NetworkResponse.class));
        }

        Collections.sort(networksResponse);
        return networksResponse;
    }

}
