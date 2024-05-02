package br.com.chonide.api.controller.development;

import br.com.chonide.api.authentication.AuthenticatedUser;
import br.com.chonide.api.controller.ApiController;
import br.com.chonide.api.controller.JsonManager;
import br.com.chonide.api.controller.ResponseEntity;
import br.com.chonide.api.dto.ReturnedFile;
import br.com.chonide.api.dto.StartMasResponse;
import br.com.chonide.domain.file.content.MasContentManager;
import br.com.chonide.domain.file.model.Mas;
import br.com.chonide.domain.file.model.Project;
import br.com.chonide.domain.model.Executor;
import br.com.chonide.domain.script.ReasoningScriptManager;

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
