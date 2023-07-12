package com.connectruck.foodtruck.auth.controller;

import com.connectruck.foodtruck.auth.annotation.Authentication;
import com.connectruck.foodtruck.auth.support.JwtTokenProvider;
import com.connectruck.foodtruck.auth.support.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws IOException {
        if (isPreflight(request)) {
            return true;
        }

        try {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (!requiresAuthentication(handlerMethod)) {
                return true;
            }

            final String token = TokenExtractor.extract(request);
            jwtTokenProvider.validateToken(token);
        } catch (ClassCastException e) {
            log.error(e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }

        return true;
    }

    private boolean isPreflight(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }

    private boolean requiresAuthentication(final HandlerMethod handlerMethod) {
        final boolean hasTypeAnnotation = handlerMethod.getBeanType().isAnnotationPresent(Authentication.class);
        final boolean hasMethodAnnotation = handlerMethod.hasMethodAnnotation(Authentication.class);
        return hasTypeAnnotation || hasMethodAnnotation;
    }
}
