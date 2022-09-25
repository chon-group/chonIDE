package org.masos.embed.sysconfig.model.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class RuntimeExecutor implements Executor {

    @Override
    public String execute(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command).redirectErrorStream(true);
            Process process = processBuilder.start();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return bufferedReader.lines().collect(Collectors.joining());
            }
        } catch (IOException e) {
            System.out.println("Não foi possível executar o comando: " + command);
        }
        return null;
    }
}
