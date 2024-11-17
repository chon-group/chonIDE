package group.chon.ide.api.domain.file.content;

import group.chon.ide.api.domain.file.FileConstants;
import group.chon.ide.api.domain.file.FileUtils;
import group.chon.ide.api.domain.model.Executor;
import group.chon.ide.api.domain.model.RuntimeExecutor;
import group.chon.ide.api.domain.model.SSHExecutor;
import group.chon.ide.api.domain.script.FirmwareScriptManager;

import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FirmwareContentManager {

    private static final String LIB_FILE_PREFIX = "lib_";

    private static final String SKETCH_FILE_NAME = "sketch.ino";

    private static final String SKETCH_BUILD_FILE = FileConstants.TMP_DIRECTORY + SKETCH_FILE_NAME;

    private static final String LIBRARY_PREFIX_PATH = FileConstants.TMP_DIRECTORY + LIB_FILE_PREFIX;

    private static final String IMPORTED_LIBRARY_MESSAGE = "Library installed";

    public static String buildSketch(String code, Executor executor) {
        File sketchFile = new File(SKETCH_BUILD_FILE);
        FileUtils.createFile(sketchFile, new ByteArrayInputStream(code));
        if (executor instanceof SSHExecutor) {
            ((SSHExecutor) executor).setResourceInRemote(sketchFile);
            sketchFile.delete();
        }
        return SKETCH_BUILD_FILE;
    }

    public static boolean importLibrary(Part submittedLibrary, Executor executor) throws IOException {
        String libraryPath = LIBRARY_PREFIX_PATH + submittedLibrary.getSubmittedFileName();
        File libraryFile = new File(libraryPath);
        FileUtils.createFile(libraryFile, submittedLibrary.getInputStream());

        String command = FirmwareScriptManager.mountArduinoImportLibScript(libraryPath);
        if (executor instanceof RuntimeExecutor) {
            String response = executor.execute(command,
                    false);
            return response.contains(IMPORTED_LIBRARY_MESSAGE);
        } else {
            ((SSHExecutor) executor).setResourceInRemote(libraryFile);
            String response = executor.execute(command,
                    false);
            return wasImportedInRemote(response);
        }
    }

    public static void deleteLibrary(String libName, Executor executor) {
        executor.execute(FirmwareScriptManager.mountRemoveArduinoLibScript(libName), false);
    }

    public static boolean isValidSubmittedLibrary(Part submittedLibrary) {
        return submittedLibrary.getSubmittedFileName().endsWith(
                FileUtils.COMPACTED_FILE_EXTENSION);
    }

    public static boolean wasImportedInRemote(String importResponse) {
        return !importResponse.contains("cannot find or open") && !importResponse.contains(
                "End-of-central-directory signature not found") && importResponse.contains("inflating:");
    }

}
