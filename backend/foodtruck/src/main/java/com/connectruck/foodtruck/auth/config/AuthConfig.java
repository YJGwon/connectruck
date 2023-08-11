package com.connectruck.foodtruck.auth.config;

import com.connectruck.foodtruck.auth.controller.AuthenticationInterceptor;
import com.connectruck.foodtruck.auth.controller.AuthenticationPrincipalArgumentResolver;
import com.connectruck.foodtruck.auth.controller.AuthorizationInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

    private static final String PATH_PATTERN_API = "/api/**";

    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthorizationInterceptor authorizationInterceptor;
    private final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns(PATH_PATTERN_API);
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns(PATH_PATTERN_API);
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationPrincipalArgumentResolver);
    }
}
