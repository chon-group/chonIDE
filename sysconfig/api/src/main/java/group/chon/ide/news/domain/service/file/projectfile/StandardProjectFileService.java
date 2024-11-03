package group.chon.ide.news.domain.service.file.projectfile;

import group.chon.ide.news.domain.model.file.Project;
import group.chon.ide.news.domain.model.file.ProjectFile;
import group.chon.ide.news.domain.model.file.embeddedmas.agent.Agent;
import group.chon.ide.news.domain.model.file.embeddedmas.environment.exogenous.Firmware;
import group.chon.ide.news.domain.repository.FileRepository;
import group.chon.ide.news.domain.repository.LocalFileRepository;

import java.io.File;

public class StandardProjectFileService implements ProjectFileService {

    private static final String ROOT = File.separator + "root" + File.separator + "chonide" + File.separator + "Projects";

    private final FileRepository fileRepository;

    private final ProjectFileCompletePathBuilder projectFileCompletePathBuilder;

    public StandardProjectFileService() {
        this.fileRepository = new LocalFileRepository();
        this.projectFileCompletePathBuilder = new ProjectFileCompletePathBuilder();
    }

    @Override
    public ProjectFile get(Project project, String name) {
        return null;
    }

    @Override
    public void save(Project project, ProjectFile projectFile) {
        String filePath = String.join(File.separator, ROOT, project.getName(), this.projectFileCompletePathBuilder.build(projectFile));
        this.fileRepository.save(filePath, projectFile.getContent());
    }

    @Override
    public void delete(Project project, ProjectFile projectFile) {
        String filePath = String.join(File.separator, ROOT, project.getName(), this.projectFileCompletePathBuilder.build(projectFile));
        this.fileRepository.delete(filePath);
    }

    @Override
    public void rename(Project project, ProjectFile projectFile, String newName) {
        String oldFilePath = String.join(File.separator, ROOT, project.getName(), this.projectFileCompletePathBuilder.build(projectFile));
        this.fileRepository.rename(oldFilePath, newName);
    }

}
