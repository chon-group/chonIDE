package group.chon.ide.api.domain.file.exception;

public class ErrorZippingFileException extends RuntimeException {

    public ErrorZippingFileException(String fileName, Throwable throwable) {
        super("Erro ao zipar o arquivo: " + fileName, throwable);
    }
}
