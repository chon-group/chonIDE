package org.masos.embed.sysconfig.controller;

import org.masos.embed.sysconfig.model.Agent;
import org.masos.embed.sysconfig.model.AgentArchClassEnum;
import org.masos.embed.sysconfig.model.Mas;
import org.masos.embed.sysconfig.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MasFilesReaderController {

    public static String getAgentName(File agentFile) {
        return agentFile.getName().replace(FileUtils.AGENT_FILE_EXTENSION, "");
    }

    public static String getAgentArchClass(String masContent, String agentName) {
        String[] masContentLines = masContent.split(FileUtils.NEW_LINE);
        for (String masContentLine : masContentLines) {
            if (masContentLine.contains(agentName)) {
                if (masContentLine.contains(MasStructureController.AGENT_ARCH_CLASS_FIELD)) {
                    if (masContentLine.contains(AgentArchClassEnum.ARGO.getName())) {
                        return AgentArchClassEnum.ARGO.getName();
                    } else if (masContentLine.contains(AgentArchClassEnum.COMMUNICATOR.getName())){
                        return AgentArchClassEnum.COMMUNICATOR.getName();
                    }
                } else {
                    return AgentArchClassEnum.JASON.getName();
                }
            }
        }
        return AgentArchClassEnum.JASON.getName();
    }

    public static Agent createDefaultAgent() {
        Agent agent = new Agent("embeddedAgent1", AgentArchClassEnum.JASON.getName(), "/* Initial beliefs and rules */\n"
                + "\n"
                + "/* Initial goals */\n"
                + "\n"
                + "!start.\n"
                + "\n"
                + "/* Plans */\n"
                + "\n"
                + "+!start <- .print(\"Hello world!\").");
        return agent;
    }

    public static Mas createDefaultMas() {
        List<Agent> masAgents = new ArrayList<>();
        masAgents.add(createDefaultAgent());
        return new Mas("mas_name", masAgents);
    }

    public static Mas getMas() {
        Mas mas = null;
        List<Agent> masAgents = new ArrayList<>();
        File masBasePath = new File(FileUtils.MAS_BASE_PATH);

        File masFolder = FileUtils.getFirstFolderInside(masBasePath);

        if (masFolder != null) {
            File masFile = FileUtils.getFirstFileInside(masFolder);
            String masContent = FileUtils.readFileContent(masFile);
            File agentFolder = FileUtils.getFirstFolderInside(masFolder);
            if (agentFolder != null) {
                File[] agentsFiles = agentFolder.listFiles();
                if (agentsFiles != null && agentsFiles.length > 0) {
                    for (File agentsFile : agentsFiles) {
                        String agentName = getAgentName(agentsFile);
                        String agentSourceCode = FileUtils.readFileContent(agentsFile);
                        String agentArchClass = getAgentArchClass(masContent, agentName);
                        Agent agent = new Agent(agentName, agentArchClass, agentSourceCode);
                        masAgents.add(agent);
                    }
                }
            }
            if (!masAgents.isEmpty()) {
                mas = new Mas(masFolder.getName(), masAgents);
            } else {
                mas = createDefaultMas();
            }
        } else {
            mas = createDefaultMas();
        }

        return mas;
    }

}
