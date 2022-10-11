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

    private static final String FAILED_IMPORT_MESSAGE
            = "Não foi possível importar, confira se o arquivo está vazio ou é válido";

    private static final String SUCCESS_IMPORT_MESSAGE = "Biblioteca importada dom sucesso";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            Part submittedLibrary = req.getPart("file");

            Response response = Response.build(resp).text();
            if (!FirmwareContentManager.isValidSubmittedLibrary(submittedLibrary)) {
                response.send(FAILED_IMPORT_MESSAGE, HttpStatus.BAD_REQUEST.getCode());
                return;
            }

            String libraryPath = FirmwareContentManager.buildLibrary(submittedLibrary, executor);
            String importResponse = executor.execute(FirmwareScriptManager.mountArduinoImportLibScript(libraryPath),
                    false);

            if (!FirmwareContentManager.wasImported(importResponse)) {
                response.send(FAILED_IMPORT_MESSAGE, HttpStatus.BAD_REQUEST.getCode());
                return;
            }

            response.ok(SUCCESS_IMPORT_MESSAGE);
        }
    }
}
