package group.chon.ide.api.api.controller.development;

import com.google.gson.JsonSyntaxException;
import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.JsonManager;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.domain.file.content.ProjectsContentManager;
import group.chon.ide.api.domain.file.exception.ProjectAlreadyExistsException;
import group.chon.ide.api.domain.file.model.Project;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/api/projects/import")
@MultipartConfig
public class MasImportController extends ApiController {

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        Part submittedProject = (Part) parameters.get("file");
        if (submittedProject == null || submittedProject.getSize() == 0) {
            return ResponseEntity.get().status(HttpServletResponse.SC_BAD_REQUEST).message("Project file is invalid");
        }
        try {
            String projectJson = new BufferedReader(
                    new InputStreamReader(submittedProject.getInputStream(), StandardCharsets.UTF_8)).lines().collect(
                    Collectors.joining(""));
            Project project = JsonManager.get().fromJson(projectJson, Project.class);
            if (project.getName().isEmpty()) {
                return ResponseEntity.get().status(HttpServletResponse.SC_BAD_REQUEST).message("Project is invalid");
            }
            if (project.getAgents() == null) {
                project.setAgents(new ArrayList<>());
            }
            if (project.getFirmwares() == null) {
                project.setFirmwares(new ArrayList<>());
            }
            if (ProjectsContentManager.isProjectNameInvalid(project.getName())) {
                return ResponseEntity.get().status(HttpServletResponse.SC_BAD_REQUEST).message(
                        "Project must have only letters");
            }
            try {
                Long createdProjectId = ProjectsContentManager.create(project.getName()).getId();
                project.setId(createdProjectId);
            } catch (ProjectAlreadyExistsException e) {
                return ResponseEntity.get().status(HttpServletResponse.SC_BAD_REQUEST).message(
                        "Project name already exists");
            }
            ProjectsContentManager.save(project);
            return ResponseEntity.get().data(project).status(HttpServletResponse.SC_CREATED).message(
                    "Project imported");
        } catch (JsonSyntaxException | IOException e) {
            return ResponseEntity.get().status(HttpServletResponse.SC_BAD_REQUEST).message(
                    "Project has invalid json structure");
        }
    }
}
