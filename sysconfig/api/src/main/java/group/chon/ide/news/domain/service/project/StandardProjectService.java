package group.chon.ide.news.domain.service.project;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.service.projectfile.ProjectFileService;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * Standard implementation of {@link ProjectService} for managing ChonIDE projects.
 * Currently, all methods are unimplemented and return default values.
 */
@RequiredArgsConstructor
public class StandardProjectService implements ProjectService {

    /**
     * Service for handling project files.
     */
    private final ProjectFileService generalProjectFileService;

    /**
     * Retrieves all projects in the system.
     * Currently, this method returns an empty list.
     *
     * @return An empty list of projects.
     */
    @Override
    public List<Project> getAll() {
        return Collections.emptyList();
    }

    /**
     * Retrieves a project by its name.
     * Currently, this method returns {@code null}.
     *
     * @param name The name of the project.
     * @return {@code null}, as this method is not implemented.
     */
    @Override
    public Project getByName(String name) {
        return null;
    }

    /**
     * Creates a new project.
     * Currently, this method is not implemented.
     *
     * @param project The project to be created.
     */
    @Override
    public void create(Project project) {
        // Not implemented
    }

    /**
     * Constructs a default project with the given name.
     * Currently, this method returns {@code null}.
     *
     * @param name The name of the default project.
     * @return {@code null}, as this method is not implemented.
     */
    public static Project mountDefaultProject(String name) {
        return null;
    }

    /**
     * Renames an existing project.
     * Currently, this method is not implemented.
     *
     * @param project The project to be renamed.
     * @param newName The new name for the project.
     */
    @Override
    public void rename(Project project, String newName) {
        // Not implemented
    }

    /**
     * Deletes a project by its name.
     * Currently, this method is not implemented.
     *
     * @param projectName The name of the project to be deleted.
     */
    @Override
    public void delete(String projectName) {
        // Not implemented
    }
}
