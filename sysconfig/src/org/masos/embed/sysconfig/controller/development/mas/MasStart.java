package org.masos.embed.sysconfig.controller.development.mas;

import org.masos.embed.sysconfig.controller.ControllerUtils;
import org.masos.embed.sysconfig.controller.JsonManager;
import org.masos.embed.sysconfig.file.content.MasContentManager;
import org.masos.embed.sysconfig.file.dto.Project;
import org.masos.embed.sysconfig.file.model.Mas;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.ReasoningScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/mas/start")
public class MasStart extends HttpServlet {

    private static final Pattern STOPING_MAS_MESSAGE_PATTERN = Pattern.compile("Encerrando SMA, processo ([\\d]+)");

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            // Importando SMA.
            Project project = JsonManager.get().fromJson(ControllerUtils.getRequestBody(req), Project.class);
             MasContentManager.buildMas(new Mas(project.getName(), project.getAgents()), executor);

            // Iniciando SMA.
            String response = executor.execute(ReasoningScriptManager.EMBEDDED_MAS_START, false);
            Matcher matcher = STOPING_MAS_MESSAGE_PATTERN.matcher(response);
            if (matcher.find()) {
                response = response.replace(matcher.group(0), "");
            }
            Response.build(resp).text().ok(response);
        }
    }
}

