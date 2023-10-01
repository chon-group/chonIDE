package org.masos.embed.sysconfig.api.controller.development;

import org.masos.embed.sysconfig.api.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.api.controller.ApiController;
import org.masos.embed.sysconfig.api.controller.JsonManager;
import org.masos.embed.sysconfig.api.controller.ResponseEntity;
import org.masos.embed.sysconfig.api.dto.ReturnedFile;
import org.masos.embed.sysconfig.api.dto.StartMasResponse;
import org.masos.embed.sysconfig.domain.file.content.MasContentManager;
import org.masos.embed.sysconfig.domain.file.model.Mas;
import org.masos.embed.sysconfig.domain.file.model.Project;
import org.masos.embed.sysconfig.domain.model.Executor;
import org.masos.embed.sysconfig.domain.script.ReasoningScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;

@WebServlet("/api/mas")
public class MasController extends ApiController {

    private static final Pattern STOPING_MAS_MESSAGE_PATTERN = Pattern.compile("Terminating the MAS, process ([\\d]+)");

    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        // Importando SMA.
        Project project = JsonManager.get().fromJson(parameters.get("data").toString(), Project.class);
        InputStream masFileInputStream = MasContentManager.getMas(new Mas(project.getName(), project.getAgents()),
                authenticatedUser.getExecutor());
        return ResponseEntity.get().data(new ReturnedFile(project.getName(), masFileInputStream)).status(
                HttpServletResponse.SC_OK);
    }

    @Override
    protected ResponseEntity put(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String action = (String) parameters.get("action");
        Executor executor = authenticatedUser.getExecutor();
        if (action.equals("start")) {
            // Importando SMA.
            Project project = JsonManager.get().fromJson(parameters.get("data").toString(), Project.class);
            MasContentManager.buildMas(new Mas(project.getName(), project.getAgents()), executor);

            // Certificando a finalização do SMA anterior.
            executor.execute(ReasoningScriptManager.EMBEDDED_MAS_STOP, false);

            // Iniciando SMA.
            return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(JsonManager.get()
                    .fromJson(executor.execute(ReasoningScriptManager.EMBEDDED_MAS_START, false),
                            StartMasResponse.class));
        } else if (action.equals("stop")) {
            String stopResponse = executor.execute(ReasoningScriptManager.EMBEDDED_MAS_STOP, false);
            if (stopResponse.isEmpty() || !STOPING_MAS_MESSAGE_PATTERN.matcher(stopResponse).find()) {
                return ResponseEntity.get().status(HttpServletResponse.SC_ACCEPTED).message(
                        "The MAS has now been terminated!");
            } else {
                return ResponseEntity.get().status(HttpServletResponse.SC_OK).message(stopResponse);
            }
        }
        return ResponseEntity.get().status(HttpServletResponse.SC_BAD_REQUEST);
    }

}
