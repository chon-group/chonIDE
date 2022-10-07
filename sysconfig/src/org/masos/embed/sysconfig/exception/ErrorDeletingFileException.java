package org.masos.embed.sysconfig.exception;

public class ErrorDeletingFileException extends RuntimeException{

    public ErrorDeletingFileException(String fileName) {
        super("Erro ao deletar o arquivo: " + fileName);
    }

}
