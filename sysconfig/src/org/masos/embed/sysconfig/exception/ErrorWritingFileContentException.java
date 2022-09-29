package org.masos.embed.sysconfig.exception;

public class ErrorWritingFileContentException extends RuntimeException {

    public ErrorWritingFileContentException(String fileName) {
        super("Erro escrevendo o conteúdo do arquivo: " + fileName);
    }

    public ErrorWritingFileContentException(String fileName, Throwable throwable) {
        super("Erro escrevendo o conteúdo do arquivo: " + fileName, throwable);
    }
}
