package org.masos.embed.sysconfig.controller;

import org.masos.embed.sysconfig.SessionManager;
import org.masos.embed.sysconfig.model.SSHConnection;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemController {

    @PostMapping("/system/poweroff")
    public void poweroff() {
        SessionManager.setUser(null);
        SSHConnection.getDefault(SessionManager.getUser()).execute("poweroff");
    }

    @PostMapping("/system/reboot")
    public void reboot() {
        SessionManager.setUser(null);
        SSHConnection.getDefault(SessionManager.getUser()).execute("reboot");
    }

}
