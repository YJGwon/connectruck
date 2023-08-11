package com.connectruck.foodtruck.auth.controller;

import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.auth.service.AuthService;
import com.connectruck.foodtruck.auth.support.TokenExtractor;
import com.connectruck.foodtruck.user.domain.Role;
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
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws IOException {
        if (isPreflight(request)) {
            return true;
        }

        try {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (!requiresAuthorization(handlerMethod)) {
                return true;
            }

            final String token = TokenExtractor.extract(request);
            final Role requiredRole = extractRequiredRole(handlerMethod);
            authService.validateRole(token, requiredRole);
        } catch (ClassCastException e) {
            log.error(e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }

        return true;
    }

    private boolean isPreflight(final HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }

    private boolean requiresAuthorization(final HandlerMethod handlerMethod) {
        final boolean hasTypeAnnotation = handlerMethod.getBeanType().isAnnotationPresent(Authorization.class);
        final boolean hasMethodAnnotation = handlerMethod.hasMethodAnnotation(Authorization.class);
        return hasTypeAnnotation || hasMethodAnnotation;
    }

    private Role extractRequiredRole(final HandlerMethod handlerMethod) {
        final Authorization authorization = handlerMethod.getBeanType().getAnnotation(Authorization.class);
        if (authorization != null) {
            return authorization.value();
        }
        return handlerMethod.getMethodAnnotation(Authorization.class).value();
    }
}
