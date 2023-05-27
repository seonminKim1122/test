package com.example.test.config;

import com.example.test.util.JwtUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0.0")
                .title("Clone Project(AirBnB)")
                .description("Api Description");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(JwtUtil.AUTHORIZATION_HEADER);

        Components components = new Components()
                .addSecuritySchemes(JwtUtil.AUTHORIZATION_HEADER, new SecurityScheme()
                        .name(JwtUtil.AUTHORIZATION_HEADER)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
