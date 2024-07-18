package com.mjolnir.yggdrasil.utilities;

import jakarta.servlet.http.HttpServletRequest;

public class WebUtils {
    public static String getRequestBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(scheme).append("://").append(serverName);

        if (serverPort != 80 && serverPort != 443) {
            baseUrl.append(":").append(serverPort);
        }

        return baseUrl.toString();
    }
}
