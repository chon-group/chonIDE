package org.masos.embed.sysconfig.controller.development.mas;

import org.masos.embed.sysconfig.controller.ApiController;
import org.masos.embed.sysconfig.controller.JsonManager;
import org.masos.embed.sysconfig.controller.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.file.content.MasContentManager;
import org.masos.embed.sysconfig.file.dto.Project;
import org.masos.embed.sysconfig.file.model.Mas;
import org.masos.embed.sysconfig.model.ResponseEntity;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.ReasoningScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/api/mas/start")
public class MasStart extends ApiController {

    private static final Pattern STOPING_MAS_MESSAGE_PATTERN = Pattern.compile("Encerrando SMA, processo ([\\d]+)");

    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        Executor executor = authenticatedUser.getExecutor();
        // Importando SMA.
        Project project = JsonManager.get().fromJson(parameters.get("project").toString(), Project.class);
        MasContentManager.buildMas(new Mas(project.getName(), project.getAgents()), executor);

        // Iniciando SMA.
        String response = executor.execute(ReasoningScriptManager.EMBEDDED_MAS_START, false);
        Matcher matcher = STOPING_MAS_MESSAGE_PATTERN.matcher(response);
        if (matcher.find()) {
            response = response.replace(matcher.group(0), "");
        }
        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(response);
    }

}

