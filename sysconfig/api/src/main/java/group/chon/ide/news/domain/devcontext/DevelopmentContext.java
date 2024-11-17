package group.chon.ide.news.domain.devcontext;

import group.chon.ide.news.domain.service.chonos.ChonosModule;
import group.chon.ide.news.domain.service.file.project.ProjectService;
import group.chon.ide.news.domain.service.file.projectfile.ProjectFileService;
import group.chon.ide.news.domain.service.mindapi.MindAPIService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DevelopmentContext {

    private final ChonosModule chonos;

    private final MindAPIService mindApi;

    private final ProjectService projects;

    private final ProjectFileService files;

    public ChonosModule chonos() {
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
