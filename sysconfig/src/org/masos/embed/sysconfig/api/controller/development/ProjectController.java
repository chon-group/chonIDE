package org.masos.embed.sysconfig.api.controller.development;

import org.masos.embed.sysconfig.api.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.controller.JsonManager;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;
import org.masos.embed.sysconfig.api.dto.ReturnedFile;
import org.masos.embed.sysconfig.domain.file.content.ProjectsContentManager;
import org.masos.embed.sysconfig.domain.file.exception.ProjectAlreadyExistsException;
import org.masos.embed.sysconfig.domain.file.exception.ProjectDoesNotExistException;
import org.masos.embed.sysconfig.domain.file.model.Project;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

@WebServlet("/api/projects")
public class ProjectController extends ApiController {

    /** Mensagem quando projeto não existe. */
    private static final String PROJECT_DOES_NOT_EXIST = "Project %s does not exist";

    /** Mensagem quando o projeto já existe. */
    private static final String PROJECT_ALREADY_EXISTS = "Project %s already exists";

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        ResponseEntity responseEntity = ResponseEntity.get();
        String projectIdField = (String) parameters.get("projectId");
        String getType = (String) parameters.get("getType");
        if (projectIdField == null || projectIdField.isEmpty()) {
            return responseEntity.data(ProjectsContentManager.getProjects()).status(HttpServletResponse.SC_OK);
        }
        long projectId;
        try {
            projectId = Long.parseLong(projectIdField);
        } catch (Exception e) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST);
        }
        if (getType.equals("1")) {
            try {
                Project project = ProjectsContentManager.get(projectId);
                return responseEntity.data(project).status(HttpServletResponse.SC_OK);
            } catch (ProjectDoesNotExistException e) {
                return responseEntity.status(HttpServletResponse.SC_NOT_FOUND).message(
                        String.format(PROJECT_DOES_NOT_EXIST, projectId));
            }
        } else if (getType.equals("2")) {
            InputStream projectInputStream = ProjectsContentManager.getFile(projectId);
            return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(
                    new ReturnedFile("", projectInputStream));
        } else {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        ResponseEntity responseEntity = ResponseEntity.get();
        String projectName = (String) parameters.get("projectName");
        if (projectName == null || projectName.isEmpty()) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST);
        }
        if (ProjectsContentManager.isProjectNameInvalid(projectName)) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).message(
                    "Project name must have only letters");
        }
        try {
            Project project = ProjectsContentManager.create(projectName);
            return responseEntity.data(project).status(HttpServletResponse.SC_OK);
        } catch (ProjectAlreadyExistsException e) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).message(PROJECT_ALREADY_EXISTS);
        }
    }

    @Override
    protected ResponseEntity put(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        Project project = JsonManager.get().fromJson(parameters.get("data").toString(), Project.class);
        try {
            ProjectsContentManager.save(project);
            return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(project);
        } catch (ProjectDoesNotExistException e) {
            return ResponseEntity.get().status(HttpServletResponse.SC_NOT_FOUND).message(
                    String.format(PROJECT_DOES_NOT_EXIST, project.getName()));
        }
    }

    @Override
    protected ResponseEntity delete(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        ResponseEntity responseEntity = ResponseEntity.get();
        String projectIdField = (String) parameters.get("projectId");
        if (projectIdField == null || projectIdField.isEmpty()) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST);
        }
        long projectId;
        try {
            projectId = Long.parseLong(projectIdField);
        } catch (Exception e) {
            return responseEntity.status(HttpServletResponse.SC_BAD_REQUEST);
        }
        try {
            ProjectsContentManager.delete(projectId);
            return responseEntity.status(HttpServletResponse.SC_OK);
        } catch (ProjectDoesNotExistException e) {
            return responseEntity.status(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
