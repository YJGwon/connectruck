package com.connectruck.foodtruck.auth.config;

import com.connectruck.foodtruck.auth.controller.AuthenticationInterceptor;
import com.connectruck.foodtruck.auth.controller.AuthorizationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

    private static final String PATH_PATTERN_API = "/api/**";

    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns(PATH_PATTERN_API);
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns(PATH_PATTERN_API);
    }
}
