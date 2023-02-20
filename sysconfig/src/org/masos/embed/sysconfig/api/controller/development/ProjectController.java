package org.masos.embed.sysconfig.api.controller.development;

import org.masos.embed.sysconfig.api.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.controller.JsonManager;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;
import org.masos.embed.sysconfig.domain.file.content.ProjectContentManager;
import org.masos.embed.sysconfig.domain.file.model.Project;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/projects")
public class ProjectController extends ApiController {

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        ResponseEntity responseEntity = ResponseEntity.get();
        try {
            return responseEntity.status(HttpServletResponse.SC_OK).data(ProjectContentManager.get());
        } catch (IOException e) {
            return responseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).message(e.getMessage());
        }
    }

    @Override
    protected ResponseEntity put(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        Project project = JsonManager.get().fromJson(parameters.get("data").toString(), Project.class);
        ProjectContentManager.set(project);
        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(project);
    }

}
