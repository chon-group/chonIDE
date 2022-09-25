package org.masos.embed.sysconfig.servlet.mas;


import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;
import org.masos.embed.sysconfig.script.ReasoningScriptManager;
import org.masos.embed.sysconfig.model.Response;

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

@WebServlet("/mas")
@MultipartConfig
public class ImportMas extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            Part file = req.getPart("file");
            File masFile = new File("/tmp/mas_" + file.getSubmittedFileName());
            if (!masFile.exists()) {
                masFile.createNewFile();
            }
            Files.copy(file.getInputStream(), masFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Response.build(resp).json().ok(SSHConnection.getDefault(user)
                    .execute(ReasoningScriptManager.mountEmbeddedMASImportScript(masFile.getAbsolutePath())));
        }
    }

}
