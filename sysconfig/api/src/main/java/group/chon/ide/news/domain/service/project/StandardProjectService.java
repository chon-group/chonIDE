package group.chon.ide.news.domain.service.project;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.service.projectfile.ProjectFileService;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class StandardProjectService implements ProjectService {

    private final ProjectFileService generalProjectFileService;

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

    public static Project mountDefaultProject(String name) {
        return null;
    }

    @Override
    public void rename(Project project, String newName) {

    }

    @Override
    public void delete(String projectName) {

    }
}
