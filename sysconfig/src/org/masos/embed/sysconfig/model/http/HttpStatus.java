package org.masos.embed.sysconfig.model.http;

public enum HttpStatus {

    OK(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404);

    private int code;

    HttpStatus(int code) {
        this.code = code;
    }

    /**
     * @return {@link #code}
     */
    public int getCode() {
        return this.code;
    }
}
