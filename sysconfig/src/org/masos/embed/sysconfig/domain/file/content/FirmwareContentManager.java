package org.masos.embed.sysconfig.domain.file.content;

import org.masos.embed.sysconfig.domain.file.FileConstants;
import org.masos.embed.sysconfig.domain.file.FileUtils;
import org.masos.embed.sysconfig.domain.file.model.Firmware;
import org.masos.embed.sysconfig.domain.model.Executor;
import org.masos.embed.sysconfig.domain.model.RuntimeExecutor;
import org.masos.embed.sysconfig.domain.model.SSHExecutor;
import org.masos.embed.sysconfig.domain.script.FirmwareScriptManager;

import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class FirmwareContentManager {

    private static final String LIB_FILE_PREFIX = "lib_";

    private static final String SKETCH_FILE_NAME = "sketch.ino";

    private static final String SKETCH_BUILD_FILE = FileConstants.TMP_DIRECTORY + SKETCH_FILE_NAME;

    private static final String LIBRARY_PREFIX_PATH = FileConstants.TMP_DIRECTORY + LIB_FILE_PREFIX;

    private static final String LIBRARY_BUILD_DIRECTORY = FileConstants.ROOT_DIRECTORY + File.separator + "Arduino"
            + File.separator + "libraries";

    public static List<Firmware> createDefaultFirmwares() {
        return Arrays.asList(new Firmware("sketch 1",
                "void setup() {\n" + "  // put your setup code here, to run once:\n" + "\n" + "}\n" + "\n"
                        + "void loop() {\n" + "  // put your main code here, to run repeatedly:\n" + "\n" + "}"));
    }

    public static String buildSketch(String code, Executor executor) {
        File sketchFile = new File(SKETCH_BUILD_FILE);
        FileUtils.createFile(sketchFile, new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8)));
        if (executor instanceof SSHExecutor) {
            ((SSHExecutor) executor).setResourceInRemote(sketchFile);
            sketchFile.delete();
        }
        return SKETCH_BUILD_FILE;
    }

    public static boolean buildLibrary(Part submittedLibrary, Executor executor) throws IOException {
        String libraryPath = LIBRARY_PREFIX_PATH + submittedLibrary.getSubmittedFileName();
        File libraryFile = new File(libraryPath);
        FileUtils.createFile(libraryFile, submittedLibrary.getInputStream());
        try {
            if (executor instanceof RuntimeExecutor) {
                return FileUtils.unzipFiles(libraryFile, LIBRARY_BUILD_DIRECTORY);
            } else {
                ((SSHExecutor) executor).setResourceInRemote(libraryFile);
                String importResponse = executor.execute(FirmwareScriptManager.mountArduinoImportLibScript(libraryPath),
                        false);
                return wasImportedInRemote(importResponse);
            }
        } finally {
            libraryFile.delete();
        }
    }

    public static boolean isValidSubmittedLibrary(Part submittedLibrary) {
        return submittedLibrary.getSize() > 0 && submittedLibrary.getSubmittedFileName().endsWith(
                FileUtils.COMPACTED_FILE_EXTENSION);
    }

    public static boolean wasImportedInRemote(String importResponse) {
        return !importResponse.contains("cannot find or open") && !importResponse.contains(
                "End-of-central-directory signature not found") && importResponse.contains("inflating:");
    }

}
