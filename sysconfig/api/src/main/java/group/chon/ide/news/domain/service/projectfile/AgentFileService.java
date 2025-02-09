package group.chon.ide.news.domain.service.projectfile;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.model.ProjectFile;
import group.chon.ide.news.domain.model.embeddedmas.EmbeddedMAS;
import group.chon.ide.news.domain.model.embeddedmas.agent.Agent;
import group.chon.ide.news.domain.model.embeddedmas.agent.AgentArchClass;
import group.chon.ide.news.domain.model.embeddedmas.masconfiguration.MasConfiguration;

/**
 * Service for handling agent files within a project.
 * Ensures that agents are properly managed within the project's MAS configuration.
 */
public class AgentFileService implements ProjectFileService {

    /**
     * Standard file service for handling general project file operations.
     */
    private final StandardProjectFileService standardFileService;

    /**
     * Constructs an {@code AgentFileService} using the provided standard file service.
     *
     * @param projectFileService The standard file service to delegate file operations.
     */
    public AgentFileService(StandardProjectFileService projectFileService) {
        this.standardFileService = projectFileService;
    }

    /**
     * Loads an agent file and updates its architecture class from the MAS configuration.
     *
     * @param project The project containing the agent.
     * @param projectFile The agent file to be loaded.
     */
    @Override
    public void load(Project project, ProjectFile projectFile) {
        this.standardFileService.load(project, projectFile);

        MasConfiguration masConfiguration = project.getEmbeddedMAS().getMasConfiguration();
        Agent agentInMasConfiguration = masConfiguration.getAgent(projectFile.getName());
        AgentArchClass archClass = agentInMasConfiguration.getArchClass();
        ((Agent) projectFile).setArchClass(archClass);
    }

    /**
     * Saves an agent file and updates the MAS configuration accordingly.
     *
     * @param project The project containing the agent.
     * @param projectFile The agent file to be saved.
     */
    @Override
    public void save(Project project, ProjectFile projectFile) {
        MasConfiguration masConfiguration = project.getEmbeddedMAS().getMasConfiguration();
        masConfiguration.saveAgent((Agent) projectFile);
        this.standardFileService.save(project, masConfiguration);
        this.standardFileService.save(project, projectFile);
    }

    /**
     * Deletes an agent file and removes it from the MAS configuration.
     *
     * @param project The project containing the agent.
     * @param projectFile The agent file to be deleted.
     */
    @Override
    public void delete(Project project, ProjectFile projectFile) {
        EmbeddedMAS embeddedMAS = project.getEmbeddedMAS();
        embeddedMAS.getMasConfiguration().removeAgent((Agent) projectFile);
        this.standardFileService.save(project, embeddedMAS.getMasConfiguration());
        this.standardFileService.delete(project, projectFile);
    }

    /**
     * Renames an agent file while maintaining its association with the project.
     *
     * @param project The project containing the agent.
     * @param projectFile The agent file to be renamed.
     * @param newName The new name for the agent file.
     */
    @Override
    public void rename(Project project, ProjectFile projectFile, String newName) {
        this.standardFileService.rename(project, projectFile, newName);
    }
}
