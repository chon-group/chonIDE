package group.chon.ide.news.domain.service.file.projectfile;

import group.chon.ide.news.domain.model.file.ProjectFile;
import group.chon.ide.news.domain.model.file.embeddedmas.agent.Agent;

public class ProjectFileCompletePathBuilder {

    public String build(ProjectFile projectFile) {
        if (projectFile instanceof Agent) {
            return "/agent/" + projectFile.getName();
        }

        return null;
    }

}
