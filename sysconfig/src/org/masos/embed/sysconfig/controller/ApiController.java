package org.masos.embed.sysconfig.controller;

import org.masos.embed.sysconfig.controller.authentication.AuthenticatedUser;
import org.masos.embed.sysconfig.model.ResponseEntity;
import org.masos.embed.sysconfig.model.http.Encoding;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class ApiController extends HttpServlet {

    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        return null;
    }

    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        return null;
    }

    protected ResponseEntity put(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        return null;
    }

    protected ResponseEntity delete(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        return null;
    }

    /**
     * Retorna um mapa com os parâmetros da requisição.
     *
     * @param request {@link HttpServletRequest} da requisição.
     * @return Mapa com os parâmetros da requisição.
     */
    private Map<String, Object> getParameters(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, Object> parameters = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            parameters.put(parameterName, request.getParameter(parameterName));
        }
        if (this.getClass().getAnnotation(MultipartConfig.class) == null) {
            return parameters;
        }
        try {
            request.getParts().stream().forEach(part -> parameters.put(part.getName(), part));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return parameters;
    }

    /**
     * Escreve o retorno do objeto de dados da requisição.
     *
     * @param resp           {@link HttpServletResponse} da requisição.
     * @param responseEntity Resposta da entidade.
     */
    private void flush(HttpServletResponse resp, ResponseEntity responseEntity) {
        if (responseEntity == null) {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        if (responseEntity.getStatus() == 0) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        resp.setCharacterEncoding(Encoding.UTF_8.getType());
        resp.setHeader("Content-Type", "application/json; charset=" + Encoding.UTF_8.getType());
        resp.setStatus(responseEntity.getStatus());
        responseEntity.date(new Date(System.currentTimeMillis()));
        try {
            PrintWriter writer = resp.getWriter();
            String jsonData = JsonManager.get().toJson(responseEntity);
            writer.write(jsonData);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) req.getAttribute("user");
        ResponseEntity responseEntity = this.get(authenticatedUser, this.getParameters(req));
        this.flush(resp, responseEntity);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) req.getAttribute("user");
        ResponseEntity responseEntity = this.post(authenticatedUser, this.getParameters(req));
        this.flush(resp, responseEntity);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) req.getAttribute("user");
        ResponseEntity responseEntity = this.put(authenticatedUser, this.getParameters(req));
        this.flush(resp, responseEntity);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) req.getAttribute("user");
        ResponseEntity responseEntity = this.delete(authenticatedUser, this.getParameters(req));
        this.flush(resp, responseEntity);
    }
}
