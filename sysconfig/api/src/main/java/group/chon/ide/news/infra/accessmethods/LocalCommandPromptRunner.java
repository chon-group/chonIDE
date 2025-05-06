package group.chon.ide.news.infra.accessmethods;

import group.chon.ide.news.app.accessmethods.CommandPromptRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class LocalCommandPromptRunner implements CommandPromptRunner {

    private static final String CHARSET = "UTF-8";

    @Override
    public String execute(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            String output;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), CHARSET))) {
                output = reader.lines().collect(Collectors.joining("\n"));
            }
            if (output.isEmpty()) {
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                    output = errorReader.lines().collect(Collectors.joining("\n"));
                }
            }
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
