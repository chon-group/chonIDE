package org.masos.embed.sysconfig.file.content;

import org.masos.embed.sysconfig.file.FileConstants;
import org.masos.embed.sysconfig.file.FileUtils;
import org.masos.embed.sysconfig.file.model.Firmware;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.model.executor.SSHExecutor;

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

    public static String buildLibrary(Part submittedLibrary, Executor executor) throws IOException {
        String libraryPath = LIBRARY_PREFIX_PATH + submittedLibrary.getSubmittedFileName();
        File libraryFile = new File(libraryPath);
        FileUtils.createFile(libraryFile, submittedLibrary.getInputStream());
        if (executor instanceof SSHExecutor) {
            ((SSHExecutor) executor).setResourceInRemote(libraryFile);
            libraryFile.delete();
        }
        return libraryPath;
    }

    public static boolean isValidSubmittedLibrary(Part submittedLibrary) {
        return submittedLibrary.getSize() > 0 && submittedLibrary.getSubmittedFileName().endsWith(
                FileUtils.COMPACTED_FILE_EXTENSION);
    }

    public static boolean wasImported(String importResponse) {
        return !importResponse.contains("cannot find or open") && !importResponse.contains(
                "End-of-central-directory signature not found") && importResponse.contains("inflating:");
    }

}
