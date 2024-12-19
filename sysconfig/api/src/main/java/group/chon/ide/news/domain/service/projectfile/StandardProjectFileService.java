package group.chon.ide.news.domain.service.projectfile;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.model.ProjectFile;
import group.chon.ide.news.domain.repository.FileRepository;
import group.chon.ide.news.domain.service.path.ProjectPathHandler;
import group.chon.ide.news.domain.service.path.ProjectPathHandlerManager;

public class StandardProjectFileService implements ProjectFileService {

    private final FileRepository fileRepository;

    private final ProjectPathHandler projectPathHandler;

    public StandardProjectFileService(FileRepository fileRepository) {
        this.projectPathHandler = ProjectPathHandlerManager.getInstance().getProjectPathHandler();
        this.fileRepository = fileRepository;
    }

    @Override
    public void load(Project project, ProjectFile projectFile) {
        String filePath = String.join(this.projectPathHandler.mountProjectPath(project), this.projectPathHandler.mountProjectFilePath(projectFile));
        String content = this.fileRepository.read(filePath);
        projectFile.setContent(content);
    }

    @Override
    public void save(Project project, ProjectFile projectFile) {
        String filePath = String.join(this.projectPathHandler.mountProjectPath(project), this.projectPathHandler.mountProjectFilePath(projectFile));
        this.fileRepository.save(filePath, projectFile.getContent());
    }

    @Override
    public void delete(Project project, ProjectFile projectFile) {
        String filePath = String.join(this.projectPathHandler.mountProjectPath(project), this.projectPathHandler.mountProjectFilePath(projectFile));
        this.fileRepository.delete(filePath);
    }

    @Override
    public void rename(Project project, ProjectFile projectFile, String newName) {
        String oldFilePath = String.join(this.projectPathHandler.mountProjectPath(project), this.projectPathHandler.mountProjectFilePath(projectFile));
        this.fileRepository.rename(oldFilePath, newName);
        projectFile.setName(newName);
    }

}
