package group.chon.ide.news.domain.service.path;

import lombok.Getter;

/**
 * Manages the {@link ProjectPathHandler} instance used for handling project paths.
 * Implements a singleton pattern to ensure a single instance is used throughout the application.
 */
public class ProjectPathHandlerManager {

    /**
     * The singleton instance of the {@link ProjectPathHandlerManager}.
     */
    @Getter
    private static ProjectPathHandlerManager instance = new ProjectPathHandlerManager();

    /**
     * The {@link ProjectPathHandler} instance responsible for managing project paths.
     */
    @Getter
    private ProjectPathHandler projectPathHandler;

    /**
     * Private constructor to enforce singleton usage.
     * Initializes the default path handler as {@link StandardProjectPathHandler}.
     */
    private ProjectPathHandlerManager() {
        this.projectPathHandler = new StandardProjectPathHandler();
    }
}
