package org.masos.embed.sysconfig.servlet.arduino;


import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;
import org.masos.embed.sysconfig.model.Response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@WebServlet("/sketchs/compile/code")
public class CompileSketchCode extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        if (user != null) {
            String boardName = req.getParameter("boardName");
            String code = req.getParameter("code");

            File sketchFile = new File("/tmp/sketch_file.ino");
            if (!sketchFile.exists()) {
                sketchFile.createNewFile();
            }
            Files.copy(new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8)), sketchFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            Response.build(resp).text().ok(SSHConnection.getDefault(user).execute(
                    FirmwareScriptManager.mountArduinoCompileSketchScript(sketchFile.getAbsolutePath(), boardName)));
        }
    }
}
