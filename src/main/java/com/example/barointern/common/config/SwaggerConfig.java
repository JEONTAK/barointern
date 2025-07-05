package com.example.barointern.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "JWT token";

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                                        .description("JWT 토큰을 입력해주세요.")
                        )
                )
                .info(apiInfo())
                .security(Collections.singletonList(new SecurityRequirement().addList(SECURITY_SCHEME_NAME)));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("springdoc-openapi")
                .packagesToScan("com.example.barointern")
                .build();
    }


    private Info apiInfo() {
        return new Info()
                .title("바로인턴 백엔드(JAVA) 과제 API Docs")
                .description("바로인턴 백엔드(JAVA) 과제 API Docs입니다.")
                .version("1.0.0");
    }
}
