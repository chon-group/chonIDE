package org.masos.embed.sysconfig.controller.development.mas;

import org.masos.embed.sysconfig.controller.JsonManager;
import org.masos.embed.sysconfig.file.content.MasContentManager;
import org.masos.embed.sysconfig.file.dto.Project;
import org.masos.embed.sysconfig.file.model.Mas;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.ReasoningScriptManager;
import org.masos.embed.sysconfig.controller.ControllerUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/mas/import")
public class MasImport extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor == null) {
            return;
        }

        Project project = JsonManager.get().fromJson(ControllerUtils.getRequestBody(req), Project.class);
        MasContentManager.buildMas(new Mas(project.getName(), project.getAgents()), executor);

        executor.execute(ReasoningScriptManager.mountEmbeddedMASImportScript(MasContentManager.MAS_FILE_NAME));
    }

}
