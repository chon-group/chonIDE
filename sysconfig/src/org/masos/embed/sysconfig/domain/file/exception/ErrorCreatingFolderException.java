package org.masos.embed.sysconfig.domain.file.exception;

public class ErrorCreatingFolderException extends RuntimeException{

    public ErrorCreatingFolderException(String folderName) {
        super("Erro na criação da pasta: " + folderName);
    }

}
