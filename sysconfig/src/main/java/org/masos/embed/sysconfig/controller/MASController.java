package org.masos.embed.sysconfig.controller;

import org.masos.embed.sysconfig.SessionManager;
import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.script.ReasoningScriptManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@RestController
public class MASController {

    @PostMapping("/mas/import")
    public String importing(@RequestParam MultipartFile file) throws IOException {
        if (SessionManager.getUser() == null) {
            return null;
        }
        File masFile = new File("/tmp/mas_" + file.getOriginalFilename());
        if (!masFile.exists()) {
            masFile.createNewFile();
        }
        Files.copy(file.getInputStream(), masFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return SSHConnection.getDefault(SessionManager.getUser()).execute(
                ReasoningScriptManager.mountEmbeddedMASImportScript(masFile.getAbsolutePath()));
    }

    @PostMapping("/mas/start")
    public String start() {
        return SSHConnection.getDefault(SessionManager.getUser()).execute(ReasoningScriptManager.EMBEDDED_MAS_START);
    }

    @PostMapping("/mas/stop")
    public String stop() {
        return SSHConnection.getDefault(SessionManager.getUser()).execute(ReasoningScriptManager.EMBEDDED_MAS_STOP);
    }

}
