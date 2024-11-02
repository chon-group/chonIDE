package group.chon.ide.newapi.domain.service;

import group.chon.ide.newapi.domain.model.Project;

public interface ProjectService {

    void create(Project project);

    void rename(Project project, String newName);

    void delete(Project project);

}
