package group.chon.ide.news.domain.service.path;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.model.ProjectFile;
import group.chon.ide.news.domain.model.embeddedmas.agent.Agent;
import group.chon.ide.news.domain.model.embeddedmas.environment.exogenous.Firmware;

import java.io.File;

/**
 * Standard implementation of {@link ProjectPathHandler} for managing project file paths.
 * Defines rules for generating paths for different types of project files.
 */
public class StandardProjectPathHandler implements ProjectPathHandler {

    /**
     * Root directory where all projects are stored.
     */
    private static final String ROOT = File.separator + "root" + File.separator + "chonide" + File.separator + "Projects";

    /**
     * Protected constructor to restrict instantiation outside package scope.
     */
    protected StandardProjectPathHandler() {
    }

    /**
     * Constructs the file system path for a given project.
     *
     * @param project The project for which the path is generated.
     * @return The project's file system path.
     */
    @Override
    public String mountProjectPath(Project project) {
        return ROOT + File.separator + project.getName();
    }

    /**
     * Constructs the file system path for a specific project file.
     * Supports different file types such as agents and firmwares.
     *
     * @param projectFile The project file for which the path is generated.
     * @return The file's system path, or {@code null} if the file type is unsupported.
     */
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
