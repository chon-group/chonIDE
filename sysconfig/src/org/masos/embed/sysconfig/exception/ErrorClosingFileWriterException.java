package org.masos.embed.sysconfig.exception;

public class ErrorClosingFileWriterException extends RuntimeException {

    public ErrorClosingFileWriterException(String fileName, Throwable throwable) {
        super("Erro fechando a escrita do arquivo: " + fileName, throwable);
    }

}
