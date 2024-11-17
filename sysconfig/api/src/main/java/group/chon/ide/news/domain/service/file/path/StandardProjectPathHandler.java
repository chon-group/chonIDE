package group.chon.ide.news.domain.service.file.path;

import group.chon.ide.news.domain.model.file.Project;
import group.chon.ide.news.domain.model.file.ProjectFile;
import group.chon.ide.news.domain.model.file.embeddedmas.agent.Agent;
import group.chon.ide.news.domain.model.file.embeddedmas.environment.exogenous.Firmware;

import java.io.File;

public class StandardProjectPathHandler implements ProjectPathHandler {

    private static final String ROOT = File.separator + "root" + File.separator + "chonide" + File.separator + "Projects";

    protected StandardProjectPathHandler() {

    }

    @Override
    public String mountProjectPath(Project project) {
        return ROOT + File.separator + project.getName();
    }

    @Override
    public String mountProjectFilePath(ProjectFile projectFile) {
        if (projectFile instanceof Agent) {
            return "/agent/" + projectFile.getName() + ".asl";
        } else if (projectFile instanceof Firmware) {
            return "/firmware/" + projectFile.getName() + ".ino";
        }

        return null;
    }

}
