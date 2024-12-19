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
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), CHARSET))) {
                return reader.lines().collect(Collectors.joining());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
