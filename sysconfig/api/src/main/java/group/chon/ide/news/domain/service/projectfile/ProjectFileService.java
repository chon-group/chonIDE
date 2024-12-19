package group.chon.ide.news.domain.service.projectfile;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.model.ProjectFile;

public interface ProjectFileService {

    void load(Project project, ProjectFile projectFile);

    void save(Project project, ProjectFile projectFile);

    void delete(Project project, ProjectFile projectFile);

    void rename(Project project, ProjectFile projectFile, String newName);

}
