package org.masos.embed.sysconfig.file.exception;

public class ErrorCreatingFileException extends RuntimeException{

    public ErrorCreatingFileException(String fileName) {
        super("Erro na criação do arquivo: " + fileName);
    }
    public ErrorCreatingFileException(String fileName, Throwable throwable) {
        super("Erro na criação do arquivo: " + fileName, throwable);
    }
}
