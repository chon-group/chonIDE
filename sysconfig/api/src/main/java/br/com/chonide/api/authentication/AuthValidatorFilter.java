package br.com.chonide.api.authentication;

import br.com.chonide.api.controller.JsonManager;
import br.com.chonide.api.controller.ResponseEntity;
import br.com.chonide.api.http.HttpContent;
import br.com.chonide.api.http.HttpEncoding;
import br.com.chonide.api.util.ResponseUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static br.com.chonide.api.authentication.AuthGeneratorServlet.EXPIRATION_TIME;

@WebFilter(urlPatterns = {"/api/*"})
public class AuthValidatorFilter implements Filter {

    /** Nome do cabeçalho de autenticação. */
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    /** Padrão do valor de cabeçalho de autenticação */
    private static final Pattern AUTHORIZATION_HEADER_VALUE_PATTERN = Pattern.compile("Bearer ([\\s\\S]+)");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String headerValue = req.getHeader(AUTHORIZATION_HEADER_NAME);

        ResponseEntity responseEntity = ResponseEntity.get();
        Date date = new Date(System.currentTimeMillis());
        resp.setCharacterEncoding(HttpEncoding.ISO_8859_1.getType());
        resp.setHeader("Content-Type", HttpContent.JSON.getType() + "; charset=" + HttpEncoding.ISO_8859_1.getType());

        if (headerValue == null || headerValue.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).message("Empty or invalid authentication header.")
                    .date(date);
            ResponseUtil.writeText(resp, JsonManager.get().toJson(responseEntity));
            resp.getOutputStream().close();
            return;
        }
        Matcher headerMatcher = AUTHORIZATION_HEADER_VALUE_PATTERN.matcher(headerValue);
        if (!headerMatcher.find()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseEntity.status(HttpServletResponse.SC_BAD_REQUEST).message("Empty or invalid authentication header.")
                    .date(date);
            ResponseUtil.writeText(resp, JsonManager.get().toJson(responseEntity));
            resp.getOutputStream().close();
            return;
        }
        String jwt = headerMatcher.group(1);
        AuthenticatedUser authenticatedUser = SecurityContextHolder.get().getAuthenticatedUsersByToken().get(jwt);
        if (authenticatedUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).message("Unable to validate token\n.").date(
                    date);
            ResponseUtil.writeText(resp, JsonManager.get().toJson(responseEntity));
            resp.getOutputStream().close();
            return;
        }
        long expirationTime = authenticatedUser.getExpirationDate().getTime();
        if (date.getTime() > expirationTime) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseEntity.status(HttpServletResponse.SC_FORBIDDEN).message("Session ended.").date(date);
            ResponseUtil.writeText(resp, JsonManager.get().toJson(responseEntity));
            resp.getOutputStream().close();
            SecurityContextHolder.get().getAuthenticatedUsersByToken().remove(jwt);
            return;
        } else {
            authenticatedUser.setExpirationDate(new Date(date.getTime() + EXPIRATION_TIME));
        }
        req.setAttribute("user", authenticatedUser);
        chain.doFilter(request, response);
    }
}
