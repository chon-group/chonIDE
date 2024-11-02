package group.chon.ide.news.domain.service.file.project;

import group.chon.ide.news.domain.model.file.Project;

import java.util.Collections;
import java.util.List;

public class StandardProjectService implements ProjectService {

    @Override
    public List<Project> getAll() {
        return Collections.emptyList();
    }

    @Override
    public Project getByName(String name) {
        return null;
    }

    @Override
    public void create(Project project) {

    }

    public Project mountDefaultProject() {
        return null;
    }

    @Override
    public void rename(Project project, String newName) {

    }

    @Override
    public void delete(String projectName) {

    }
}
