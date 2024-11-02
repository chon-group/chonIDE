package group.chon.ide.newapi.domain.service;

import group.chon.ide.newapi.domain.model.Project;
import group.chon.ide.newapi.domain.model.ProjectFile;

public interface ProjectFileService {

    void save(Project project, ProjectFile projectFile);

    void delete(Project project, ProjectFile projectFile);

    void rename(Project project, String oldName, ProjectFile projectFile);


}
