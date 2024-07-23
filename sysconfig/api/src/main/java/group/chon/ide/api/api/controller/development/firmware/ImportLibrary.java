package group.chon.ide.api.api.controller.development.firmware;

import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.domain.file.content.FirmwareContentManager;
import group.chon.ide.api.api.controller.ResponseEntity;

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
            = "Unable to import, check if the file is empty or valid";

    private static final String SUCCESS_IMPORT_MESSAGE = "Library imported from success";

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        Part submittedLibrary = (Part) parameters.get("file");

        ResponseEntity responseEntity = ResponseEntity.get();
        if (!FirmwareContentManager.isValidSubmittedLibrary(submittedLibrary)) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).data(FAILED_IMPORT_MESSAGE);
        }

        boolean wasImported = false;
        try {
            wasImported = FirmwareContentManager.importLibrary(submittedLibrary, authenticatedUser.getExecutor());
        } catch (IOException ignored) {
        }

        if (!wasImported) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).data(FAILED_IMPORT_MESSAGE);
        }

        return responseEntity.status(HttpServletResponse.SC_OK).data(SUCCESS_IMPORT_MESSAGE);
    }

}
