package group.chon.ide.news.domain.service.projectfile;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.model.ProjectFile;
import group.chon.ide.news.domain.model.embeddedmas.agent.Agent;
import lombok.RequiredArgsConstructor;

/**
 * Intercepts project file operations and delegates them to the appropriate service.
 * Uses {@link AgentFileService} for agent-related files and {@link StandardProjectFileService} for all other files.
 */
@RequiredArgsConstructor
public class IntercepterProjectFileService implements ProjectFileService {

    /**
     * Standard file service for handling general project files.
     */
    private final StandardProjectFileService standardProjectFileService;

    /**
     * Specialized file service for handling agent files.
     */
    private final AgentFileService agentFileService;

    /**
     * Loads a project file using the appropriate file service.
     *
     * @param project The project to which the file belongs.
     * @param projectFile The file to be loaded.
     */
    @Override
    public void load(Project project, ProjectFile projectFile) {
        this.build(projectFile).load(project, projectFile);
    }

    /**
     * Saves a project file using the appropriate file service.
     *
     * @param project The project to which the file belongs.
     * @param projectFile The file to be saved.
     */
    @Override
    public void save(Project project, ProjectFile projectFile) {
        this.build(projectFile).save(project, projectFile);
    }

    /**
     * Deletes a project file using the appropriate file service.
     *
     * @param project The project to which the file belongs.
     * @param projectFile The file to be deleted.
     */
    @Override
    public void delete(Project project, ProjectFile projectFile) {
        this.build(projectFile).delete(project, projectFile);
    }

    /**
     * Renames a project file using the appropriate file service.
     *
     * @param project The project to which the file belongs.
     * @param projectFile The file to be renamed.
     * @param newName The new name for the file.
     */
    @Override
    public void rename(Project project, ProjectFile projectFile, String newName) {
        this.build(projectFile).rename(project, projectFile, newName);
    }

    /**
     * Determines the appropriate file service based on the type of project file.
     * If the file is an instance of {@link Agent}, it uses {@link AgentFileService},
     * otherwise, it uses {@link StandardProjectFileService}.
     *
     * @param projectFile The file to be processed.
     * @return The appropriate {@link ProjectFileService} implementation.
     */
    private ProjectFileService build(ProjectFile projectFile) {
        if (projectFile instanceof Agent) {
            return this.agentFileService;
        } else {
            return standardProjectFileService;
        }
    }
}
