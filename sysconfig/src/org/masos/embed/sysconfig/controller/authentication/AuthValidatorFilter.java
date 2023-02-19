package org.masos.embed.sysconfig.controller.authentication;

import org.masos.embed.sysconfig.controller.JsonManager;
import org.masos.embed.sysconfig.model.ResponseEntity;
import org.masos.embed.sysconfig.model.http.Encoding;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(urlPatterns = {"/api/*"})
public class AuthValidatorFilter implements Filter {

    /** Nome do cabeçalho de autenticação. */
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    /** Padrão do valor de cabeçalho de autenticação */
    private static final Pattern AUTHORIZATION_HEADER_VALUE_PATTERN = Pattern.compile("Bearer ([\\s\\S]+)");

    public AuthValidatorFilter() {
    }

    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String headerValue = req.getHeader(AUTHORIZATION_HEADER_NAME);

        ResponseEntity responseEntity = ResponseEntity.get();
        Date date = new Date(System.currentTimeMillis());
        resp.setCharacterEncoding(Encoding.ISO_8859_1.getType());
        resp.setHeader("Content-Type", "application/json; charset=" + Encoding.ISO_8859_1.getType());

        PrintWriter writer = resp.getWriter();
        if (headerValue == null || headerValue.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).message(
                    "Cabeçalho de autenticação vazio ou inválido.").date(date);
            writer.write(JsonManager.get().toJson(responseEntity));
            writer.flush();
            writer.close();
            return;
        }
        Matcher headerMatcher = AUTHORIZATION_HEADER_VALUE_PATTERN.matcher(headerValue);
        if (!headerMatcher.find()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).message(
                    "Cabeçalho de autenticação vazio ou inválido.").date(date);
            writer.write(JsonManager.get().toJson(responseEntity));
            writer.flush();
            writer.close();
            return;
        }
        String jwt = headerMatcher.group(1);
        AuthenticatedUser authenticatedUser = SecurityContextHolder.get().getAuthenticatedUsersByToken().get(jwt);
        if (authenticatedUser == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseEntity.status(HttpServletResponse.SC_FORBIDDEN).message("Não foi possível validar o token.").date(
                    date);
            writer.write(JsonManager.get().toJson(responseEntity));
            writer.flush();
            writer.close();
            return;
        }
        req.setAttribute("user", authenticatedUser);
        chain.doFilter(request, response);
    }
}
