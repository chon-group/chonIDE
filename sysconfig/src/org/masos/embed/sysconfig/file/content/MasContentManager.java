package org.masos.embed.sysconfig.file.content;

import org.masos.embed.sysconfig.file.FileConstants;
import org.masos.embed.sysconfig.file.FileUtils;
import org.masos.embed.sysconfig.file.exception.ErrorCreatingFileException;
import org.masos.embed.sysconfig.file.exception.ErrorCreatingFolderException;
import org.masos.embed.sysconfig.file.exception.ErrorWritingFileContentException;
import org.masos.embed.sysconfig.file.model.Agent;
import org.masos.embed.sysconfig.file.model.AgentArchClass;
import org.masos.embed.sysconfig.file.model.Mas;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.model.executor.SSHExecutor;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MasContentManager {

    private static final String AGENT_FILE_EXTENSION = ".asl";

    private static final String AGENT_DIRECTORY_NAME = "asl";

    private static final String DEFAULT_MAS_NAME = "mas_project";

    private static final String MAS_DIRECTORY_NAME = "mas_project";

    private static final String MAS_DIRECTORY_PATH = FileConstants.TMP_DIRECTORY + MAS_DIRECTORY_NAME;

    private static final String MAS_FILE_PATH = MAS_DIRECTORY_PATH + FileUtils.COMPACTED_FILE_EXTENSION;

    public static String buildMas(Mas mas, Executor executor) {
        File masDirectory = new File(MAS_DIRECTORY_PATH);
        if (!FileUtils.createFolder(masDirectory)) {
            throw new ErrorCreatingFolderException(MAS_DIRECTORY_PATH);
        }

        File masStructureFile = new File(masDirectory, mas.getName() + MasStructure.MAS_STRUCTURE_FILE_EXTENSION);
        if (!FileUtils.createFile(masStructureFile)) {
            throw new ErrorCreatingFileException(masStructureFile.getName());
        }
        FileUtils.writeFileContent(masStructureFile,
                new MasStructure(mas.getName(), mas.getAgents()).getCompleteStructure());

        File agentDirectory = new File(masDirectory, AGENT_DIRECTORY_NAME);
        if (!FileUtils.createFolder(agentDirectory)) {
            throw new ErrorCreatingFolderException(agentDirectory.getName());
        }
        for (Agent agent : mas.getAgents()) {
            File agentFile = new File(agentDirectory, agent.getName() + AGENT_FILE_EXTENSION);
            if (!FileUtils.createFile(agentFile)) {
                throw new ErrorCreatingFileException(agentFile.getName());
            }
            if (!FileUtils.writeFileContent(agentFile, agent.getSourceCode())) {
                throw new ErrorWritingFileContentException(agentFile.getName());
            }
        }

        File masBuild = FileUtils.zipFolder(masDirectory);
        if (executor instanceof SSHExecutor) {
            ((SSHExecutor) executor).setResourceInRemote(masBuild);
            FileUtils.deleteFile(masBuild);
            FileUtils.deleteFolder(masDirectory);
        }

        return MAS_FILE_PATH;
    }

    protected static Mas createDefaultMas() {
        return new Mas(DEFAULT_MAS_NAME, Arrays.asList(createDefaultAgent()));
    }

    private static Agent createDefaultAgent() {
        return new Agent("embeddedAgent1", AgentArchClass.JASON.getName(),
                "/* Initial beliefs and rules */\n" + "\n" + "/* Initial goals */\n" + "\n" + "!start.\n" + "\n"
                        + "/* Plans */\n" + "\n" + "+!start <- .print(\"Hello world!\").");
    }

    private static class MasStructure {

        public static final String MAS_STRUCTURE_FILE_EXTENSION = ".mas2j";

        private static final String AGENT_ARCH_CLASS_FIELD = "agentArchClass";

        private static final String NEW_LINE = "\n";

        private static final String SPACE = " ";

        private static final String TAB = "\t";

        private static final String COLON = ":";

        private static final String SEMI_COLON = ";";

        private static final String OPEN_BRACE = "{";

        private static final String CLOSE_BRASE = "}";

        private static final String MAS_FIELD = "MAS";

        private static final String INFRASTRUCTURE_FIELD = "infrastructure";

        private static final String AGENTS_FIELD = "agents";

        private static final String AGENT_SOURCE_PATH_FIELD = "aslSourcePath";

        private static final String DEFAULT_INFRASTRUCTURE = "Centralised";

        private static final String DEFAULT_AGENTS_SOURCE = "\"asl\"";

        private String masName;

        private List<Agent> agents;

        public MasStructure(String masName, List<Agent> agents) {
            this.masName = masName;
            this.agents = agents;
        }

        private StringBuffer getCommonStructure() {
            StringBuffer structure = new StringBuffer();
            structure.append(MAS_FIELD).append(SPACE).append(masName).append(SPACE).append(OPEN_BRACE).append(NEW_LINE);
            structure.append(TAB).append(INFRASTRUCTURE_FIELD).append(COLON).append(SPACE).append(
                    DEFAULT_INFRASTRUCTURE).append(NEW_LINE);
            structure.append(TAB).append(AGENTS_FIELD).append(COLON).append(NEW_LINE);
            for (Agent agent : agents) {
                structure.append(TAB).append(TAB).append(agent.getName());
                if (!agent.getArchClass().equals(AgentArchClass.JASON.getName())) {
                    structure.append(SPACE).append(AGENT_ARCH_CLASS_FIELD).append(SPACE).append(agent.getArchClass());
                }
                structure.append(SEMI_COLON).append(NEW_LINE);
            }

            return structure;
        }

        private String getCompleteStructure() {
            StringBuffer structure = getCommonStructure();
            structure.append(TAB).append(AGENT_SOURCE_PATH_FIELD).append(COLON).append(NEW_LINE);
            structure.append(TAB).append(TAB).append(DEFAULT_AGENTS_SOURCE).append(SEMI_COLON).append(NEW_LINE);
            structure.append(CLOSE_BRASE).append(NEW_LINE);

            return structure.toString();
        }

        private String getPartialStructure() {
            StringBuffer structure = getCommonStructure();
            structure.append(CLOSE_BRASE).append(NEW_LINE);

            return structure.toString();
        }

    }

}
