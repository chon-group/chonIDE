package group.chon.ide.news.domain.service.file.projectfile;

import group.chon.ide.news.domain.model.file.Project;
import group.chon.ide.news.domain.model.file.ProjectFile;
import group.chon.ide.news.domain.model.file.embeddedmas.EmbeddedMAS;
import group.chon.ide.news.domain.model.file.embeddedmas.agent.Agent;
import group.chon.ide.news.domain.model.file.embeddedmas.agent.AgentArchClass;
import group.chon.ide.news.domain.model.file.embeddedmas.masconfiguration.MasConfiguration;

public class AgentFileService implements ProjectFileService {

    private final StandardProjectFileService standardFileService;

    public AgentFileService(StandardProjectFileService projectFileService) {
        this.standardFileService = projectFileService;
    }

    @Override
    public void load(Project project, ProjectFile projectFile) {
        this.standardFileService.load(project, projectFile);

        MasConfiguration masConfiguration = project.getEmbeddedMAS().getMasConfiguration();
        Agent agentInMasConfiguration = masConfiguration.getAgent(projectFile.getName());
        AgentArchClass archClass = agentInMasConfiguration.getArchClass();
        ((Agent) projectFile).setArchClass(archClass);
    }

    @Override
    public void save(Project project, ProjectFile projectFile) {
        MasConfiguration masConfiguration = project.getEmbeddedMAS().getMasConfiguration();
        masConfiguration.saveAgent((Agent) projectFile);
        this.standardFileService.save(project, masConfiguration);
        this.standardFileService.save(project, projectFile);
    }

    @Override
    public void delete(Project project, ProjectFile projectFile) {
        EmbeddedMAS embeddedMAS = project.getEmbeddedMAS();
        embeddedMAS.getMasConfiguration().removeAgent((Agent) projectFile);
        this.standardFileService.save(project, embeddedMAS.getMasConfiguration());
        this.standardFileService.delete(project, projectFile);
    }


    @Override
    public void rename(Project project, ProjectFile projectFile, String newName) {
        this.standardFileService.rename(project, projectFile, newName);
    }

}
