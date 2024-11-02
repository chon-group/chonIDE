package group.chon.ide.newapi.domain.service;

import group.chon.ide.newapi.domain.model.Project;
import group.chon.ide.newapi.domain.model.ProjectFile;
import group.chon.ide.newapi.domain.repository.FileRepository;

import java.io.File;

public class StandardProjectFileService implements ProjectFileService {

    private static final String ROOT = File.separator + "root" + File.separator + "chonide" + File.separator + "Projects";

    private FileRepository fileRepository;


    @Override
    public void save(Project project, ProjectFile projectFile) {
        String filePath = String.join(File.separator, ROOT, project.getName(), projectFile.getFile());
        this.fileRepository.save(filePath, projectFile.getContent());
    }

    @Override
    public void delete(Project project, ProjectFile projectFile) {
        String filePath = String.join(File.separator, ROOT, project.getName(), projectFile.getFile());
        this.fileRepository.delete(filePath);
    }

    @Override
    public void rename(Project project, String oldName, ProjectFile projectFile) {
        String oldFilePath = String.join(File.separator, ROOT, project.getName(), oldName);
        String newFilePath = String.join(File.separator, ROOT, project.getName(), projectFile.getFile());
        this.fileRepository.rename(oldFilePath, newFilePath);
    }

}
