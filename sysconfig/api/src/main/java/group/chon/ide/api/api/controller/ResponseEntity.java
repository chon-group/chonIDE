package group.chon.ide.api.api.controller;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResponseEntity {

    /** Formatador de data. */
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /** Objeto de resposta. */
    @Getter
    private Object data;

    /** Status da requisição. */
    @Getter
    private int status;

    /** Mensagem da requisição. */
    @Getter
    private String message;

    /** Data e horário da requisição. */
    @Getter
    private String date;

    /**
     * Retorna uma instância de {@link ResponseEntity}
     *
     * @return Instância de {@link ResponseEntity}.
     */
    public static ResponseEntity get() {
        return new ResponseEntity();
    }

    /**
     * Seta o objeto de resposta.
     *
     * @param data Objeto de resposta.
     */
    public ResponseEntity data(Object data) {
        if (data == null) {
            throw new NullPointerException();
        }
        this.data = data;
        return this;
    }

    /**
     * Seta a mensagem da resposta.
     *
     * @param message Mensagem.
     */
    public ResponseEntity message(String message) {
        if (message == null) {
            throw new NullPointerException();
        }
        this.message = message;
        return this;
    }

    /**
     * Seta o status da requisição.
     *
     * @param status Status da requisição.
     */
    public ResponseEntity status(int status) {
        this.status = status;
        return this;
    }

    /**
     * Seta a data e o tempo da requisição.
     *
     * @param date Data e tempo.
     */
    public ResponseEntity date(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }
        this.date = DATE_FORMATTER.format(date);
        return this;
    }

}
