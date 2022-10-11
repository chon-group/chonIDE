package org.masos.embed.sysconfig.controller.development;

import org.masos.embed.sysconfig.controller.JsonManager;
import org.masos.embed.sysconfig.file.content.ProjectContentManager;
import org.masos.embed.sysconfig.file.dto.Project;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.model.http.HttpStatus;
import org.masos.embed.sysconfig.controller.ControllerUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/projects")
public class ProjectController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response = Response.build(resp).allowAnyOrigin();
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor == null) {
            return;
        }

        Project project = (Project) req.getSession().getAttribute("project");
        if (project == null) {
            project = ProjectContentManager.get();
            req.getSession().setAttribute("project", project);
        }
        response.json().ok(project);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        Response response = Response.build(resp).allowAnyOrigin();
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor == null) {
            return;
        }

        Project project = JsonManager.get().fromJson(ControllerUtils.getRequestBody(req), Project.class);
        if (project != null) {
            ProjectContentManager.set(project);
            req.getSession().setAttribute("project", project);
        } else {
            response.json().status(HttpStatus.BAD_REQUEST);
        }
    }
}
