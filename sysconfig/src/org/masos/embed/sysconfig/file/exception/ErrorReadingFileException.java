package org.masos.embed.sysconfig.file.exception;

public class ErrorReadingFileException extends RuntimeException{

    public ErrorReadingFileException(String fileName) {
        super("Erro lendo o conteúdo do arquivo: " + fileName);
    }

    public ErrorReadingFileException(String fileName, Throwable throwable) {
        super("Erro lendo o conteúdo do arquivo: " + fileName, throwable);
    }

}
