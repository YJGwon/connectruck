package com.connectruck.foodtruck.auth.controller;

import com.connectruck.foodtruck.auth.annotation.Authentication;
import com.connectruck.foodtruck.auth.support.AuthorizationExtractor;
import com.connectruck.foodtruck.auth.support.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
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
            final String token = AuthorizationExtractor.extract(request);
            jwtTokenProvider.validateToken(token);
        }

        return true;
    }

    private boolean isPreflight(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }

    private boolean requiresAuthentication(final Object handler) {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Authentication classAnnotation = handlerMethod.getBeanType().getAnnotation(Authentication.class);
        final Authentication methodAnnotation = handlerMethod.getMethodAnnotation(Authentication.class);
        return Objects.nonNull(classAnnotation) || Objects.nonNull(methodAnnotation);
    }
}
