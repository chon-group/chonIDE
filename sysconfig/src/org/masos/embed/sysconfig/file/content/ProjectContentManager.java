package org.masos.embed.sysconfig.file.content;

import org.masos.embed.sysconfig.controller.JsonManager;
import org.masos.embed.sysconfig.file.dto.Project;
import org.masos.embed.sysconfig.file.model.Mas;
import org.masos.embed.sysconfig.file.FileConstants;
import org.masos.embed.sysconfig.file.FileUtils;

import java.io.File;
import java.io.IOException;

public class ProjectContentManager {

    private static final String PROJECT_JSON_FILE_NAME = "project.json";

    private static File projectDirectory;

    private static File projectJson;

    public static Project get() throws IOException {
        if (projectDirectory == null) {
            projectDirectory = new File(FileConstants.BASE_PATH);
        }

        if (!projectDirectory.exists()) {
            projectDirectory.mkdirs();
        }
        projectJson = new File(FileConstants.BASE_PATH, PROJECT_JSON_FILE_NAME);
        // Primeira uso.
        if (!projectJson.exists()) {
            projectJson.createNewFile();
            Mas mas = MasContentManager.createDefaultMas();
            Project project = new Project(mas.getName(), mas.getAgents(),
                    FirmwareContentManager.createDefaultFirmwares());
            set(project);
            return project;
        }

        return JsonManager.get().fromJson(FileUtils.readFileContent(projectJson), Project.class);
    }

    public static void set(Project project) {
        FileUtils.writeFileContent(projectJson, JsonManager.get().toJson(project));
    }

}
