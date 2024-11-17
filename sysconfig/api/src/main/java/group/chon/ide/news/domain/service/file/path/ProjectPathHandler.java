package group.chon.ide.news.domain.service.file.path;

import group.chon.ide.news.domain.model.file.Project;
import group.chon.ide.news.domain.model.file.ProjectFile;

public interface ProjectPathHandler {

    String mountProjectPath(Project project);

    String mountProjectFilePath(ProjectFile projectFile);

}
