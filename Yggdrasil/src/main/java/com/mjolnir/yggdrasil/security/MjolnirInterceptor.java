package com.mjolnir.yggdrasil.security;

import com.mjolnir.yggdrasil.entities.MjolnirApiKey;
import com.mjolnir.yggdrasil.repositories.MjolnirKeyRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MjolnirInterceptor implements HandlerInterceptor {
    private final MjolnirKeyRepository keyRepository;

    public MjolnirInterceptor(MjolnirKeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
//        String apiKey = request.getHeader("MJOlNIR-API-KEY");
//        if (apiKey == null) {
//            if ("GET".equals(request.getMethod())) {
//                return true; // Allow GET requests without an API key
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return false;
//            }
//        }
//
//        MjolnirApiKey key = keyRepository.findByApiKey(apiKey);
//        if (key == null) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return false;
//        }
//
//        request.setAttribute("role", key.getRole());
        return true; // comment out to return true as I think we're replacing this with Spring Security - Liam
    }
}
