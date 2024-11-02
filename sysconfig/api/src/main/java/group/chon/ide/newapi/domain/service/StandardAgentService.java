package group.chon.ide.newapi.domain.service;

import group.chon.ide.newapi.domain.model.Agent;
import group.chon.ide.newapi.domain.model.Project;
import group.chon.ide.newapi.domain.model.ProjectFile;

public class StandardAgentService implements ProjectFileService {

    private StandardProjectFileService standardProjectFileService;

    @Override
    public void save(Project project, ProjectFile projectFile) {
        project.getMas2J().saveAgent((Agent) projectFile);
        this.standardProjectFileService.save(project, project.getMas2J());
        this.standardProjectFileService.save(project, projectFile);
    }

    @Override
    public void delete(Project project, ProjectFile projectFile) {
        project.getMas2J().removeAgent((Agent) projectFile);
        this.standardProjectFileService.save(project, project.getMas2J());
        this.standardProjectFileService.delete(project, projectFile);
    }

    @Override
    public void rename(Project project, String oldName, ProjectFile projectFile) {
        this.standardProjectFileService.rename(project, oldName, projectFile);
    }

}
