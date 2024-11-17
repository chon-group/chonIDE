package group.chon.ide.news.domain.service.file.path;

import lombok.Getter;

public class ProjectPathHandlerManager {

    @Getter
    private static ProjectPathHandlerManager instance = new ProjectPathHandlerManager();

    @Getter
    private ProjectPathHandler projectPathHandler;

    private ProjectPathHandlerManager() {
        this.projectPathHandler = new StandardProjectPathHandler();
    }

}
