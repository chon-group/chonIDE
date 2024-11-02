package group.chon.ide.news.domain.service.file.projectfile;

import group.chon.ide.news.domain.model.file.Project;
import group.chon.ide.news.domain.model.file.ProjectFile;

public interface ProjectFileService {

    ProjectFile get(Project project, String name);

    void save(Project project, ProjectFile projectFile);

    void delete(Project project, ProjectFile projectFile);

    void rename(Project project, ProjectFile projectFile, String newName);

}
