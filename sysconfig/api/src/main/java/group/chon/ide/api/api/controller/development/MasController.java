package group.chon.ide.api.api.controller.development;

import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.JsonManager;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.api.api.dto.ReturnedFile;
import group.chon.ide.api.api.dto.StartMasResponse;
import group.chon.ide.api.domain.file.content.MasContentManager;
import group.chon.ide.api.domain.file.model.Mas;
import group.chon.ide.api.domain.file.model.Project;
import group.chon.ide.api.domain.model.Executor;
import group.chon.ide.api.domain.script.ReasoningScriptManager;

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
