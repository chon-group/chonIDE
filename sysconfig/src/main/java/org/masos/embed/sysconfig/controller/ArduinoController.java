package org.masos.embed.sysconfig.controller;

import org.masos.embed.sysconfig.SessionManager;
import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@RestController
public class ArduinoController {

    @PostMapping("/library/import")
    public boolean importLibrary(@RequestParam MultipartFile file) throws Exception {
        if (SessionManager.getUser() == null) {
            return false;
        }

        File libraryFile = new File("/tmp/lib_" + file.getOriginalFilename());
        if (!libraryFile.exists()) {
            libraryFile.createNewFile();
        }
        Files.copy(file.getInputStream(), libraryFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        String response = SSHConnection.getDefault(SessionManager.getUser()).execute(
                FirmwareScriptManager.mountArduinoImportLibScript(libraryFile.getAbsolutePath()));

        if (response.contains("cannot find or open") || response.contains(
                "End-of-central-directory signature not found")) {
            return false;
        }

        return true;
    }

    @PostMapping("/sketch/compile/code")
    public String compileSketch(@RequestParam String code, @RequestParam String boardName) throws Exception {
        if (SessionManager.getUser() == null) {
            return null;
        }

        File sketchFile = new File("/tmp/sketch_file.ino");
        if (!sketchFile.exists()) {
            sketchFile.createNewFile();
        }
        Files.copy(new ByteArrayInputStream(code.getBytes()), sketchFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return SSHConnection.getDefault(SessionManager.getUser()).execute(
                FirmwareScriptManager.mountArduinoCompileSketchScript(sketchFile.getAbsolutePath(), boardName));
    }

    @PostMapping("/sketch/compile/file")
    public String compileSketch(@RequestParam MultipartFile file, @RequestParam String boardName) throws Exception {
        if (SessionManager.getUser() == null) {
            return null;
        }

        File sketchFile = new File("/tmp/sketch_file.ino");
        if (!sketchFile.exists()) {
            sketchFile.createNewFile();
        }
        Files.copy(file.getInputStream(), sketchFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return SSHConnection.getDefault(SessionManager.getUser()).execute(
                FirmwareScriptManager.mountArduinoCompileSketchScript(sketchFile.getAbsolutePath(), boardName));
    }

    @PostMapping("/sketch/deploy")
    public String deploySketch(@RequestParam String boardName, @RequestParam String serialPort) {
        return SSHConnection.getDefault(SessionManager.getUser()).execute(
                FirmwareScriptManager.mountArduinoDeploySketchScript(boardName, serialPort));
    }

    @GetMapping("/libraries")
    public String getAllLibraries() {
        return SSHConnection.getDefault(SessionManager.getUser()).execute(FirmwareScriptManager.ARDUINO_LIST_LIBRARIES);
    }

    @GetMapping("/boards")
    public String getAllBoards() {
        return SSHConnection.getDefault(SessionManager.getUser()).execute(FirmwareScriptManager.ARDUINO_LIST_BOARDS);
    }

}
