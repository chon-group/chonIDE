package org.masos.embed.sysconfig.servlet.arduino;


import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@WebServlet("/libraries/import")
@MultipartConfig
public class ImportLibrary extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            Part file = req.getPart("file");

            File libraryFile = new File("/tmp/lib_" + file.getSubmittedFileName());
            if (!libraryFile.exists()) {
                libraryFile.createNewFile();
            }
            Files.copy(file.getInputStream(), libraryFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            String response = SSHConnection.getDefault(user).execute(
                    FirmwareScriptManager.mountArduinoImportLibScript(libraryFile.getAbsolutePath()));

            if (response.contains("cannot find or open") || response.contains(
                    "End-of-central-directory signature not found")) {
                Response.build(resp).json().ok(false);
                return;
            }

            Response.build(resp).json().ok(true);
        }
    }
}
