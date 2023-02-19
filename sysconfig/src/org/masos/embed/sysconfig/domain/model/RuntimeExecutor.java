package org.masos.embed.sysconfig.domain.model;

import org.masos.embed.sysconfig.domain.file.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class RuntimeExecutor implements Executor {

    @Override
    public String execute(String command, boolean mantainLineBreak) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String output = reader.lines().collect(Collectors.joining());
                if (!mantainLineBreak) {
                    return output.replace(FileUtils.BREAK_LINE, "");
                }
                return output;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
