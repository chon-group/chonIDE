package org.masos.embed.sysconfig.model;

import org.masos.embed.sysconfig.controller.JsonManager;
import org.masos.embed.sysconfig.model.annotation.ResponseObject;
import org.masos.embed.sysconfig.model.http.Encoding;
import org.masos.embed.sysconfig.model.http.HttpContent;
import org.masos.embed.sysconfig.model.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

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

    public Response status(HttpStatus status) {
        this.response.setStatus(status.getCode());
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

    public Response allowAnyOrigin() {
        this.response.addHeader("Access-Control-Allow-Origin", "*");
        return this;
    }

    public void ok(Object response) {
        if (response instanceof List) {
            Object firstObject = ((Collection) response).toArray()[0];
            if (firstObject.getClass().getAnnotation(ResponseObject.class) != null) {
                this.send(JsonManager.get().toJson(response), HttpStatus.OK.getCode());
                return;
            }
        } else if (response.getClass().getAnnotation(ResponseObject.class) != null) {
            this.send(JsonManager.get().toJson(response), HttpStatus.OK.getCode());
            return;
        }
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
            writer.println(response.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
