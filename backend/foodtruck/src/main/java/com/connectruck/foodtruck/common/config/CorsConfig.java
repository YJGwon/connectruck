package com.connectruck.foodtruck.common.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String allowedOrigin;

    public CorsConfig(@Value("${connectruck.cors.allowed-origin}") final String allowedOrigin) {
        this.allowedOrigin = allowedOrigin;
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigin)
                .allowedMethods(getAllHttpMethodNames());
    }

    private String[] getAllHttpMethodNames() {
        return Arrays.stream(HttpMethod.values())
                .map(HttpMethod::name)
                .toArray(String[]::new);
    }
}
