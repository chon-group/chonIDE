package group.chon.ide.news.domain.service.projectfile;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.model.ProjectFile;
import group.chon.ide.news.domain.model.embeddedmas.agent.Agent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IntercepterProjectFileService implements ProjectFileService {

    private final StandardProjectFileService standardProjectFileService;

    private final AgentFileService agentFileService;

    @Override
    public void load(Project project, ProjectFile projectFile) {
        this.build(projectFile).load(project, projectFile);
    }

    @Override
    public void save(Project project, ProjectFile projectFile) {
        this.build(projectFile).save(project, projectFile);
    }

    @Override
    public void delete(Project project, ProjectFile projectFile) {
        this.build(projectFile).delete(project, projectFile);
    }

    @Override
    public void rename(Project project, ProjectFile projectFile, String newName) {
        this.build(projectFile).rename(project, projectFile, newName);
    }

    private ProjectFileService build(ProjectFile projectFile) {
        if (projectFile instanceof Agent) {
            return this.agentFileService;
        } else {
            return standardProjectFileService;
        }
    }
}
