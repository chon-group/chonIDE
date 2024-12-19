package group.chon.ide.news.app.devcontext;

import group.chon.ide.news.app.service.chonos.ChonosModules;
import group.chon.ide.news.domain.service.project.ProjectService;
import group.chon.ide.news.domain.service.projectfile.ProjectFileService;
import group.chon.ide.news.app.service.mindapi.MindAPIService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DevelopmentContext {

    private final ChonosModules chonos;

    private final MindAPIService mindApi;

    private final ProjectService projects;

    private final ProjectFileService files;

    public ChonosModules chonos() {
        return this.chonos;
    }

    public MindAPIService mindApi() {
        return this.mindApi;
    }

    public ProjectService projects() {
        return this.projects;
    }

    public ProjectFileService files() {
        return this.files;
    }

}
