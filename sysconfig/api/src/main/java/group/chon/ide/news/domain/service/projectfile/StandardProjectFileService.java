package group.chon.ide.news.domain.service.projectfile;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.model.ProjectFile;
import group.chon.ide.news.domain.repository.FileRepository;
import group.chon.ide.news.domain.service.path.ProjectPathHandler;
import group.chon.ide.news.domain.service.path.ProjectPathHandlerManager;

/**
 * Standard implementation of {@link ProjectFileService} for managing project files in ChonIDE.
 * Handles file operations such as loading, saving, deleting, and renaming using a {@link FileRepository}.
 */
public class StandardProjectFileService implements ProjectFileService {

    /**
     * Repository for handling file operations.
     */
    private final FileRepository fileRepository;

    /**
     * Path handler for determining project file locations.
     */
    private final ProjectPathHandler projectPathHandler;

    /**
     * Constructs a {@code StandardProjectFileService} with the given file repository.
     * Uses the {@link ProjectPathHandler} from the {@link ProjectPathHandlerManager}.
     * 
     * @param fileRepository The file repository to handle file operations.
     */
    public StandardProjectFileService(FileRepository fileRepository) {
        this.projectPathHandler = ProjectPathHandlerManager.getInstance().getProjectPathHandler();
        this.fileRepository = fileRepository;
    }

    /**
     * Loads the content of a project file into memory.
     * 
     * @param project The project to which the file belongs.
     * @param projectFile The file to be loaded.
     */
    @Override
    public void load(Project project, ProjectFile projectFile) {
        String filePath = String.join(
            this.projectPathHandler.mountProjectPath(project), 
            this.projectPathHandler.mountProjectFilePath(projectFile)
        );
        String content = this.fileRepository.read(filePath);
        projectFile.setContent(content);
    }

    /**
     * Saves a project file to disk.
     * 
     * @param project The project to which the file belongs.
     * @param projectFile The file to be saved.
     */
    @Override
    public void save(Project project, ProjectFile projectFile) {
        String filePath = String.join(
            this.projectPathHandler.mountProjectPath(project), 
            this.projectPathHandler.mountProjectFilePath(projectFile)
        );
        this.fileRepository.save(filePath, projectFile.getContent());
    }

    /**
     * Deletes a project file from disk.
     * 
     * @param project The project to which the file belongs.
     * @param projectFile The file to be deleted.
     */
    @Override
    public void delete(Project project, ProjectFile projectFile) {
        String filePath = String.join(
            this.projectPathHandler.mountProjectPath(project), 
            this.projectPathHandler.mountProjectFilePath(projectFile)
        );
        this.fileRepository.delete(filePath);
    }

    /**
     * Renames a project file on disk and updates its name in memory.
     * 
     * @param project The project to which the file belongs.
     * @param projectFile The file to be renamed.
     * @param newName The new name for the file.
     */
    @Override
    public void rename(Project project, ProjectFile projectFile, String newName) {
        String oldFilePath = String.join(
            this.projectPathHandler.mountProjectPath(project), 
            this.projectPathHandler.mountProjectFilePath(projectFile)
        );
        this.fileRepository.rename(oldFilePath, newName);
        projectFile.setName(newName);
    }
}
