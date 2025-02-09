package group.chon.ide.news.domain.service.project;

import group.chon.ide.news.domain.model.Project;

import java.util.List;

/**
 * Service for managing ChonIDE projects.
 */
public interface ProjectService {

    /**
     * Retrieves all projects in the system.
     *
     * @return A list of all projects.
     */
    List<Project> getAll();

    /**
     * Retrieves a project by its name.
     *
     * @param name The name of the project.
     * @return The corresponding project.
     */
    Project getByName(String name);

    /**
     * Creates a new project.
     *
     * @param project The project to be created.
     */
    void create(Project project);

    /**
     * Renames an existing project.
     *
     * @param project The project to be renamed.
     * @param newName The new name for the project.
     */
    void rename(Project project, String newName);

    /**
     * Deletes a project by its name.
     *
     * @param name The name of the project to be deleted.
     */
    void delete(String name);
}
