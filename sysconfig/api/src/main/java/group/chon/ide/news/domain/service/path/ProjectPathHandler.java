package group.chon.ide.news.domain.service.path;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.model.ProjectFile;

public interface ProjectPathHandler {

    String mountProjectPath(Project project);

    String mountProjectFilePath(ProjectFile projectFile);

}
