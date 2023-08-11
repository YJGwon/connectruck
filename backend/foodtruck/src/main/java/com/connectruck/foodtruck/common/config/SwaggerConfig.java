package com.connectruck.foodtruck.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.time.LocalTime;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

@Configuration
public class SwaggerConfig {
    static {
        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class, new Schema<String>().example("HH:mm"));
        SpringDocUtils.getConfig().replaceWithClass(ErrorResponse.class, ProblemDetail.class);
    }

    @Bean
    public OpenAPI openAPI() {

        final Info info = new Info()
                .version("v0.1.0")
                .title("Connectruck API")
                .description("행사장 푸드트럭 주문 시스템 connectruck api 문서");

        final String tokenSchemaName = "access token";
        final SecurityRequirement tokenRequirement = new SecurityRequirement().addList(tokenSchemaName);
        final Components components = new Components()
                .addSecuritySchemes(tokenSchemaName, new SecurityScheme()
                        .name(tokenSchemaName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(tokenRequirement)
                .components(components);
    }
}
