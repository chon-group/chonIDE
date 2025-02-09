package group.chon.ide.news.domain.service.path;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.model.ProjectFile;

/**
 * Handles path management for projects and project files.
 * Provides methods to generate paths for a project and its files.
 */
public interface ProjectPathHandler {

    /**
     * Constructs the file system path for the given project.
     * 
     * @param project The project for which the path is generated.
     * @return The project's file system path.
     */
    String mountProjectPath(Project project);

    /**
     * Constructs the file system path for a specific project file.
     * 
     * @param projectFile The project file for which the path is generated.
     * @return The file's file system path.
     */
    String mountProjectFilePath(ProjectFile projectFile);
}
