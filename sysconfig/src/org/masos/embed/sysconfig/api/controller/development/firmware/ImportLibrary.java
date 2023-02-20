package org.masos.embed.sysconfig.api.controller.development.firmware;

import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.domain.file.content.FirmwareContentManager;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/libraries/import")
@MultipartConfig
public class ImportLibrary extends ApiController {

    private static final String FAILED_IMPORT_MESSAGE
            = "Não foi possível importar, confira se o arquivo está vazio ou é válido";

    private static final String SUCCESS_IMPORT_MESSAGE = "Biblioteca importada dom sucesso";

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        Part submittedLibrary = (Part) parameters.get("file");

        ResponseEntity responseEntity = ResponseEntity.get();
        if (!FirmwareContentManager.isValidSubmittedLibrary(submittedLibrary)) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).data(FAILED_IMPORT_MESSAGE);
        }

        boolean wasImported = false;
        try {
            wasImported = FirmwareContentManager.buildLibrary(submittedLibrary, authenticatedUser.getExecutor());
        } catch (IOException ignored) {
        }

        if (!wasImported) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).data(FAILED_IMPORT_MESSAGE);
        }

        return responseEntity.status(HttpServletResponse.SC_OK).data(SUCCESS_IMPORT_MESSAGE);
    }

}
