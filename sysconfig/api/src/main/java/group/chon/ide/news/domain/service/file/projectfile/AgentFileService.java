package group.chon.ide.news.domain.service.file.projectfile;

import group.chon.ide.news.domain.model.file.Project;
import group.chon.ide.news.domain.model.file.ProjectFile;
import group.chon.ide.news.domain.model.file.embeddedmas.agent.Agent;
import group.chon.ide.news.domain.model.file.embeddedmas.agent.AgentArchClass;

public class AgentFileService implements ProjectFileService {

    private final StandardProjectFileService standardProjectFileService;

    public AgentFileService() {
        this.standardProjectFileService = new StandardProjectFileService();
    }

    @Override
    public ProjectFile get(Project project, String name) {
        Agent agentInMasConfiguration = project.getEmbeddedMAS().getMasConfiguration().getAgent(name);
        AgentArchClass archClass = agentInMasConfiguration.getArchClass();

        Agent agentInProject = (Agent) this.standardProjectFileService.get(project, name);
        agentInProject.setArchClass(archClass);

        return agentInProject;
    }

    @Override
    public void save(Project project, ProjectFile projectFile) {
        project.getEmbeddedMAS().getMasConfiguration().saveAgent((Agent) projectFile);
        this.standardProjectFileService.save(project, project.getEmbeddedMAS().getMasConfiguration());
        this.standardProjectFileService.save(project, projectFile);
    }

    @Override
    public void delete(Project project, ProjectFile projectFile) {
        project.getEmbeddedMAS().getMasConfiguration().removeAgent((Agent) projectFile);
        this.standardProjectFileService.save(project, project.getEmbeddedMAS().getMasConfiguration());
        this.standardProjectFileService.delete(project, projectFile);
    }


    @Override
    public void rename(Project project, ProjectFile projectFile, String newName) {
        this.standardProjectFileService.rename(project, projectFile, newName);
    }

}
