package com.connectruck.foodtruck.config;

import io.swagger.v3.oas.models.media.Schema;
import java.time.LocalTime;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

@Configuration
public class SwaggerConfig {
    static {
        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class, new Schema<String>().example("HH:mm"));
        SpringDocUtils.getConfig().replaceWithClass(ErrorResponse.class, ProblemDetail.class);
    }
}
