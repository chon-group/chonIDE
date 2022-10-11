package org.masos.embed.sysconfig.controller;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ControllerUtils {

    public static String getRequestBody(HttpServletRequest request) {
        try {
            return new BufferedReader(new InputStreamReader(request.getInputStream())).lines().collect(
                    Collectors.joining());
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean isRefreshRequest(HttpServletRequest request) {
        try {
            Object refreshAttribute = request.getParameter("refresh");
            if (refreshAttribute != null) {
                return Boolean.parseBoolean(refreshAttribute.toString());
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
