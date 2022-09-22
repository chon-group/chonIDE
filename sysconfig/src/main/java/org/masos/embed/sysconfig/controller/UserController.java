package org.masos.embed.sysconfig.controller;

import com.google.gson.Gson;
import org.masos.embed.sysconfig.SessionManager;
import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;
import org.masos.embed.sysconfig.model.response.DomainStatus;
import org.masos.embed.sysconfig.script.ConnectionScriptManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PostMapping("/user/connect")
    public boolean startSession(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null
                || user.getPassword().isEmpty()) {
            return false;
        }
        SSHConnection sshConnection = SSHConnection.getDefault(user);
        if (sshConnection.test()) {
            SessionManager.setUser(user);
            return true;
        }
        return false;
    }

    @PostMapping("/user/disconnect")
    public void disconnect() {
        SessionManager.setUser(null);
    }

    @GetMapping("/user/connect")
    public boolean isLogged() {
        if (SessionManager.getUser() != null) {
            return true;
        }
        return false;
    }

    @GetMapping("/user/first-access")
    public boolean domainStatus() {
        String response = SSHConnection.getDefault(SessionManager.getUser()).execute(
                ConnectionScriptManager.DDNS_STATUS);
        DomainStatus domainStatus = new Gson().fromJson(response, DomainStatus.class);

        if (domainStatus.getDomain().equals("myrobot.bot.chon.group") && domainStatus.getLocalAddress().equals(
                "192.168.168.1")) {
            return true;
        }

        return false;
    }

}
