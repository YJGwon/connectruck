package com.connectruck.foodtruck.auth.controller;

import com.connectruck.foodtruck.auth.annotation.Authentication;
import com.connectruck.foodtruck.auth.support.JwtTokenProvider;
import com.connectruck.foodtruck.auth.support.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {
        if (isPreflight(request)) {
            return true;
        }

        if (requiresAuthentication(handler)) {
            final String token = TokenExtractor.extract(request);
            jwtTokenProvider.validateToken(token);
        }

        return true;
    }

    private boolean isPreflight(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }

    private boolean requiresAuthentication(final Object handler) {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final boolean hasTypeAnnotation = handlerMethod.getBeanType().isAnnotationPresent(Authentication.class);
        final boolean hasMethodAnnotation = handlerMethod.hasMethodAnnotation(Authentication.class);
        return hasTypeAnnotation || hasMethodAnnotation;
    }
}
