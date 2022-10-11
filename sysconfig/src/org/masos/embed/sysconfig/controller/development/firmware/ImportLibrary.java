package org.masos.embed.sysconfig.controller.development.firmware;

import org.masos.embed.sysconfig.file.content.FirmwareContentManager;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.model.http.HttpStatus;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet("/libraries/import")
@MultipartConfig
public class ImportLibrary extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            Part submittedLibrary = req.getPart("file");

            Response response = Response.build(resp).text();
            if (!FirmwareContentManager.isValidSubmittedLibrary(submittedLibrary)) {
                response.send("Não foi possível importar a biblioteca, confira a validade da mesma",
                        HttpStatus.BAD_REQUEST.getCode());
                return;
            }

            String libraryPath = FirmwareContentManager.buildLibrary(submittedLibrary, executor);

            String importResponse = executor.execute(FirmwareScriptManager.mountArduinoImportLibScript(libraryPath));

            if (importResponse.contains("cannot find or open") || importResponse.contains(
                    "End-of-central-directory signature not found")) {
                response.send("Não foi possível importar a biblioteca, confira a validade da mesma",
                        HttpStatus.BAD_REQUEST.getCode());
                return;
            }

            response.ok("Biblioteca importada dom sucesso");
        }
    }
}
