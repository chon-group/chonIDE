package org.masos.embed.sysconfig.domain.file.content;

import org.masos.embed.sysconfig.api.controller.JsonManager;
import org.masos.embed.sysconfig.domain.file.FileConstants;
import org.masos.embed.sysconfig.domain.file.FileUtils;
import org.masos.embed.sysconfig.domain.file.exception.ProjectAlreadyExistsException;
import org.masos.embed.sysconfig.domain.file.exception.ProjectDoesNotExistException;
import org.masos.embed.sysconfig.domain.file.model.Project;
import org.masos.embed.sysconfig.domain.file.model.ProjectsMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.file.Files.*;

/**
 * Classe responsável pelo gerenciamento dos projetos da chonIDE.
 */
public class ProjectsContentManager {

    /** Diretório principal da chonIDE. */
    private static final Path CHONIDE_MAIN_DIRETORY_PATH = Paths.get(FileConstants.ROOT_DIRECTORY + "chonide");

    /** Nome do diretório dos projetos da chonIDE. */
    private static final Path PROJECTS_DIRECTORY_PATH = CHONIDE_MAIN_DIRETORY_PATH.resolve("Projects");

    /** Nome do arquivo de mapeamento dos projetos. */
    private static final Path PROJECTS_MAP_PATH = PROJECTS_DIRECTORY_PATH.resolve("projects.json");

    /** Nome do arquivo com o projeto. */
    private static final String PROJECT_FILE = "project.json";

    static {
        if (!exists(PROJECTS_DIRECTORY_PATH)) {
            try {
                createDirectories(PROJECTS_DIRECTORY_PATH);
                createFile(PROJECTS_MAP_PATH);
                FileUtils.write(PROJECTS_MAP_PATH, JsonManager.get().toJson(new ProjectsMapping()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Cria um projeto.
     *
     * @param projectName Nome do projeto.
     */
    public static Project create(String projectName) {
        Project project = new Project(projectName, new ArrayList<>(), new ArrayList<>());
        Path projectFolder = PROJECTS_DIRECTORY_PATH.resolve(projectName);
        if (!exists(projectFolder)) {
            try {
                createDirectories(projectFolder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new ProjectAlreadyExistsException();
        }
        Path projectFile = projectFolder.resolve(PROJECT_FILE);
        try {
            createFile(projectFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            write(projectFile, JsonManager.get().toJson(project).getBytes());
            ProjectsMapping projectsMapping = getProjectMapping();
            long id = projectsMapping.getNextId();
            projectsMapping.getProjects().put(id, project.getName());
            saveProjectMapping(projectsMapping);
            project.setId(id);
            return project;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isProjectNameInvalid(String projectName) {
        return !Pattern.compile("^([a-zA-z]+)$").matcher(projectName).find();
    }

    /**
     * Salva um projeto.
     *
     * @param project Projeto.
     */
    public static void save(Project project) {
        ProjectsMapping projectsMapping = getProjectMapping();
        Map<Long, String> projects = projectsMapping.getProjects();
        String projectName = projects.get(project.getId());
        if (projectName == null) {
            throw new ProjectDoesNotExistException();
        }
        Path projectFolder = PROJECTS_DIRECTORY_PATH.resolve(projectName);
        if (!exists(projectFolder)) {
            throw new ProjectDoesNotExistException();
        }
        Path projectFile = projectFolder.resolve(PROJECT_FILE);
        if (project.getAgents().isEmpty() && project.getFirmwares().isEmpty()) {
            Project fileProject = get(project.getId());
            project.setAgents(fileProject.getAgents());
            project.setFirmwares(fileProject.getFirmwares());
        }
        try {
            FileUtils.write(projectFile, JsonManager.get().toJson(project));
            if (!projectName.equals(project.getName())) {
                projects.remove(projectName);
                projects.put(project.getId(), project.getName());
                saveProjectMapping(projectsMapping);

                move(projectFolder, PROJECTS_DIRECTORY_PATH.resolve(project.getName()));
                Files.delete(projectFolder);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retorna um projeto a partir do nome.
     *
     * @param projectId Id do projeto.
     * @return Projeto.
     */
    public static Project get(long projectId) {
        ProjectsMapping projectsMapping = getProjectMapping();
        String projectName = projectsMapping.getProjects().get(projectId);
        if (projectName == null) {
            throw new ProjectDoesNotExistException();
        }
        Path projectFolder = PROJECTS_DIRECTORY_PATH.resolve(projectName);
        if (!exists(projectFolder)) {
            throw new ProjectDoesNotExistException();
        }
        Path projectFile = projectFolder.resolve(PROJECT_FILE);
        try {
            return JsonManager.get().fromJson(lines(projectFile).collect(Collectors.joining()), Project.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retorna um projeto a partir do nome.
     *
     * @param projectId Id do projeto.
     * @return Projeto.
     */
    public static InputStream getFile(long projectId) {
        ProjectsMapping projectsMapping = getProjectMapping();
        String projectName = projectsMapping.getProjects().get(projectId);
        if (projectName == null) {
            throw new ProjectDoesNotExistException();
        }
        Path projectFolder = PROJECTS_DIRECTORY_PATH.resolve(projectName);
        if (!exists(projectFolder)) {
            throw new ProjectDoesNotExistException();
        }
        Path projectFile = projectFolder.resolve(PROJECT_FILE);
        try {
            return Files.newInputStream(projectFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retorna o id e o nome dos projetos.
     *
     * @return Id e nome dos projetos.
     */
    public static List<Project> getProjects() {
        List<Project> projects = new ArrayList<>();
        ProjectsMapping projectMapping = getProjectMapping();
        for (Map.Entry<Long, String> projectEntry : projectMapping.getProjects().entrySet()) {
            projects.add(
                    new Project(projectEntry.getKey(), projectEntry.getValue(), new ArrayList<>(), new ArrayList<>()));
        }
        return projects;
    }

    /**
     * Deleta um projeto a partir do nome.
     *
     * @param projectId Id do projeto.
     */
    public static void delete(long projectId) {
        String projectName = getProjectMapping().getProjects().get(projectId);
        Path projectFolder = PROJECTS_DIRECTORY_PATH.resolve(projectName);
        if (exists(projectFolder)) {
            FileUtils.deleteAll(projectFolder);
            ProjectsMapping projectMapping = getProjectMapping();
            projectMapping.getProjects().remove(projectId);
            saveProjectMapping(projectMapping);
        } else {
            throw new ProjectDoesNotExistException();
        }
    }

    /**
     * Retorna o conteúdo do arquivo de mapeamento de projetos.
     *
     * @return {@link ProjectsMapping}.
     */
    private static ProjectsMapping getProjectMapping() {
        try {
            return JsonManager.get().fromJson(lines(PROJECTS_MAP_PATH).collect(Collectors.joining()),
                    ProjectsMapping.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Salva o mapeamento de projetos.
     *
     * @param projectsMapping {@link ProjectsMapping}.
     */
    private static void saveProjectMapping(ProjectsMapping projectsMapping) {
        FileUtils.write(PROJECTS_MAP_PATH, JsonManager.get().toJson(projectsMapping));
    }

}
