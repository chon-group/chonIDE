package org.masos.embed.sysconfig.file.content;

import org.masos.embed.sysconfig.controller.JsonManager;
import org.masos.embed.sysconfig.file.FileConstants;
import org.masos.embed.sysconfig.file.FileUtils;
import org.masos.embed.sysconfig.file.dto.Project;
import org.masos.embed.sysconfig.file.model.Mas;

import java.io.File;
import java.io.IOException;

public class ProjectContentManager {

    private static final String PROJECT_DIRECTORY_NAME = "chonide";

    private static final String PROJECT_DIRECTORY_PATH = FileConstants.ROOT_DIRECTORY + PROJECT_DIRECTORY_NAME;

    private static final String PROJECT_FILE_NAME = "project.json";

    private static final String PROJECT_FILE_PATH = PROJECT_DIRECTORY_PATH + File.separator + PROJECT_FILE_NAME;

    private static File projectDirectory;

    private static File projectFile;

    public static Project get() throws IOException {
        if (projectDirectory == null) {
            projectDirectory = new File(PROJECT_DIRECTORY_PATH);
        }

        if (!projectDirectory.exists()) {
            projectDirectory.mkdirs();
        }
        projectFile = new File(PROJECT_FILE_PATH);
        // Primeira uso.
        if (!projectFile.exists()) {
            projectFile.createNewFile();
            Mas mas = MasContentManager.createDefaultMas();
            Project project = new Project(mas.getName(), mas.getAgents(),
                    FirmwareContentManager.createDefaultFirmwares());
            set(project);
            return project;
        }

        return JsonManager.get().fromJson(FileUtils.readFileContent(projectFile), Project.class);
    }

    public static void set(Project project) {
        FileUtils.writeFileContent(projectFile, JsonManager.get().toJson(project));
    }

}
