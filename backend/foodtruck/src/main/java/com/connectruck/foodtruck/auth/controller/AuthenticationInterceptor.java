package com.connectruck.foodtruck.auth.controller;

import com.connectruck.foodtruck.auth.annotation.Authentication;
import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.auth.service.AuthService;
import com.connectruck.foodtruck.auth.support.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

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
            if (!requiresAuthentication(handlerMethod)) {
                return true;
            }

            final String token = TokenExtractor.extract(request);
            authService.validateToken(token);

            if (requiresAuthorization(handlerMethod)) {
                final Authorization annotation = extractAnnotation(handlerMethod, Authorization.class);
                authService.validateRole(token, annotation.role());
            }
        } catch (ClassCastException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }

        return true;
    }

    private boolean isPreflight(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }

    private boolean requiresAuthentication(final HandlerMethod handlerMethod) {
        return hasAnnotation(handlerMethod, Authentication.class);
    }

    private boolean requiresAuthorization(final HandlerMethod handlerMethod) {
        return hasAnnotation(handlerMethod, Authorization.class);
    }

    private <A extends Annotation> boolean hasAnnotation(final HandlerMethod handlerMethod,
                                                         final Class<A> annotationType) {
        final boolean hasTypeAnnotation = handlerMethod.getBeanType().isAnnotationPresent(annotationType);
        final boolean hasMethodAnnotation = handlerMethod.hasMethodAnnotation(annotationType);
        return hasTypeAnnotation || hasMethodAnnotation;
    }

    private <A extends Annotation> A extractAnnotation(final HandlerMethod handlerMethod,
                                                       final Class<A> annotationType) {
        final A typeAnnotation = handlerMethod.getBeanType().getAnnotation(annotationType);
        if (typeAnnotation != null) {
            return typeAnnotation;
        }
        return handlerMethod.getMethodAnnotation(annotationType);
    }
}
