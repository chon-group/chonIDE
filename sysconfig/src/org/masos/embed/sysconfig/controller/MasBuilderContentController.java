package org.masos.embed.sysconfig.controller;

import org.masos.embed.sysconfig.exception.ErrorCreatingFileException;
import org.masos.embed.sysconfig.exception.ErrorCreatingFolderException;
import org.masos.embed.sysconfig.exception.ErrorWritingFileContentException;
import org.masos.embed.sysconfig.model.Agent;
import org.masos.embed.sysconfig.utils.FileUtils;

import java.io.File;

public class MasBuilderContentController {

	public static File buildMas(MasStructureController masStructureController) {
		File masFile = null;
		File masFolder = new File(FileUtils.getMasPath(masStructureController.getMasName()));

		if (masFolder.exists()) {
			FileUtils.deleteFile(masFolder);
		}

		if (FileUtils.createFolder(masFolder)) {
			File masStructureFile = new File(masFolder, masStructureController.getMasName() + FileUtils.MAS_STRUCTURE_FILE_EXTENSION);
			if (FileUtils.createFile(masStructureFile)) {
				if (FileUtils.writeFileContent(masStructureFile, masStructureController.getCompleteStructure())) {
					File agentFolder = new File(masFolder, FileUtils.AGENT_FOLDER);
					if(FileUtils.createFolder(agentFolder)) {
						for (Agent agent : masStructureController.getAgents()) {
							File agentFile = new File(agentFolder, agent.getName() + FileUtils.AGENT_FILE_EXTENSION);
							if (FileUtils.createFile(agentFile)) {
								if(!FileUtils.writeFileContent(agentFile, agent.getSourceCode())) {
									throw new ErrorWritingFileContentException(agentFile.getName());
								}
							} else {
								throw new ErrorCreatingFileException(agentFile.getName());
							}
						}
					} else {
						throw new ErrorCreatingFolderException(agentFolder.getName());
					}
				} else {
					throw new ErrorWritingFileContentException(masStructureFile.getName());
				}
			} else {
				throw new ErrorCreatingFileException(masStructureFile.getName());
			}
		} else {
			throw new ErrorCreatingFolderException(masFolder.getName());
		}

		if (FileUtils.zipFolder(masFolder)) {
			masFile = new File(masFolder.getAbsoluteFile() + FileUtils.COMPACTED_FILE_EXTENSION);
		}

		return masFile;
		
	}

}
