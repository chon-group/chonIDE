package org.masos.embed.sysconfig.servlet.arduino;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
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

@WebServlet("/sketchs/compile/file")
@MultipartConfig
public class CompileSketchFile extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            String boardName = req.getParameter("boardName");
            Part file = req.getPart("file");

            File sketchFile = new File("/tmp/sketch_file.ino");
            if (!sketchFile.exists()) {
                sketchFile.createNewFile();
            }
            Files.copy(file.getInputStream(), sketchFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            Response.build(resp).text().ok(executor.execute(
                    FirmwareScriptManager.mountArduinoCompileSketchScript(sketchFile.getAbsolutePath(), boardName)));
        }
    }
}
