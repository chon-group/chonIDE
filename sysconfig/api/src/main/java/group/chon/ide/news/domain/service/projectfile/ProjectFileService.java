package group.chon.ide.news.domain.service.projectfile;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.model.ProjectFile;

/**
 * Service for managing project files within a ChonIDE project.
 * Provides methods for loading, saving, deleting, and renaming files associated with a project.
 */
public interface ProjectFileService {

    /**
     * Loads a project file into the given project.
     *
     * @param project The project to which the file belongs.
     * @param projectFile The file to be loaded.
     */
    void load(Project project, ProjectFile projectFile);

    /**
     * Saves a project file within the given project.
     *
     * @param project The project to which the file belongs.
     * @param projectFile The file to be saved.
     */
    void save(Project project, ProjectFile projectFile);

    /**
     * Deletes a project file from the given project.
     *
     * @param project The project to which the file belongs.
     * @param projectFile The file to be deleted.
     */
    void delete(Project project, ProjectFile projectFile);

    /**
     * Renames a project file within the given project.
     *
     * @param project The project to which the file belongs.
     * @param projectFile The file to be renamed.
     * @param newName The new name for the file.
     */
    void rename(Project project, ProjectFile projectFile, String newName);
}
