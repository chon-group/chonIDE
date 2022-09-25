package org.masos.embed.sysconfig.model;

import org.masos.embed.sysconfig.model.http.Encoding;
import org.masos.embed.sysconfig.model.http.HttpContent;
import org.masos.embed.sysconfig.model.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Response {

    private HttpServletResponse response;

    private Response(HttpServletResponse response) {
        this.response = response;
    }

    public static Response build(HttpServletResponse response) {
        return new Response(response);
    }

    public Response contentType(String contentType) {
        this.response.setContentType(contentType);
        return this;
    }

    public Response characterEnconding(String characterEncoding) {
        this.response.setCharacterEncoding(characterEncoding);
        return this;
    }

    public Response status(int status) {
        this.response.setStatus(status);
        return this;
    }

    public Response json() {
        this.response.setContentType(HttpContent.JSON.getType());
        return this;
    }

    public Response text() {
        this.response.setContentType(HttpContent.TEXT.getType());
        return this;
    }

    public void ok(Object response) {
        this.send(response, HttpStatus.OK.getCode());
    }

    public void badRequest() {
        this.response.setStatus(HttpStatus.BAD_REQUEST.getCode());
    }

    public void unauthorized() {
        this.response.setStatus(HttpStatus.UNAUTHORIZED.getCode());
    }

    public void notFound() {
        this.response.setStatus(HttpStatus.NOT_FOUND.getCode());
    }

    public void send(Object response, int status) {
        if (this.response.getCharacterEncoding() == null) {
            this.response.setCharacterEncoding(Encoding.UTF_8.getType());
        }
        this.response.setStatus(status);
        try {
            PrintWriter writer = this.response.getWriter();
            writer.println(response);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
