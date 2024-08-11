package group.chon.ide.api.api.controller.system;

import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;
import lombok.AllArgsConstructor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/system/attributes")
public class SystemAttributes extends ApiController {

    public SystemAttributes() {
        super(false);
    }

    private List<String> getUsers() {
        List<String> users = new ArrayList<>();
        try {
            // Comando para obter informações dos usuários
            String[] command = {"bash", "-c", "cat /etc/passwd | grep bash | cut -d ':' -f 1"};

            // Executa o comando
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            // Lê a saída do comando
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                users.add(line);
            }

            // Aguarda o término do processo
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    protected ResponseEntity get(Map<String, Object> parameters) {
        return ResponseEntity.get().status(HttpServletResponse.SC_OK).data(new Attributes(this.getUsers()));
    }

    @AllArgsConstructor
    private static class Attributes {

        /**
         * Usuário do computador.
         */
        private final List<String> users;

    }

}
