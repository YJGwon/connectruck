package com.connectruck.foodtruck.auth.controller;

import com.connectruck.foodtruck.auth.annotation.Authentication;
import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.auth.service.AuthService;
import com.connectruck.foodtruck.auth.support.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
                             final Object handler) {
        if (isPreflight(request) || !requiresAuthentication(handler)) {
            return true;
        }

        final String token = TokenExtractor.extract(request);
        authService.validateToken(token);

        if (requiresAuthorization(handler)) {
            final Authorization annotation = extractAnnotation(handler, Authorization.class);
            authService.validateRole(token, annotation.role());
        }

        return true;
    }

    private boolean isPreflight(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }

    private boolean requiresAuthentication(final Object handler) {
        return hasAnnotation(handler, Authentication.class);
    }

    private boolean requiresAuthorization(final Object handler) {
        return hasAnnotation(handler, Authorization.class);
    }

    private <A extends Annotation> boolean hasAnnotation(final Object handler, final Class<A> annotationType) {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final boolean hasTypeAnnotation = handlerMethod.getBeanType().isAnnotationPresent(annotationType);
        final boolean hasMethodAnnotation = handlerMethod.hasMethodAnnotation(annotationType);
        return hasTypeAnnotation || hasMethodAnnotation;
    }

    private <A extends Annotation> A extractAnnotation(final Object handler, final Class<A> annotationType) {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final A typeAnnotation = handlerMethod.getBeanType().getAnnotation(annotationType);
        if (typeAnnotation != null) {
            return typeAnnotation;
        }
        return handlerMethod.getMethodAnnotation(annotationType);
    }
}
