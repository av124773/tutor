package com.gtalent.tutor.configs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info().title("tutor apis 範例")
                    .description("gtelant 課堂測試 Swagger docs 描述")
                    .version("v2.0")
                    .license(new License().name("your license"))
                )
                .components(new Components()
                    .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                    )
                );
                    
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder().group("public-apis")
                .pathsToMatch("/jwt/**",
                        "/v2/users/**",
                        "/products/**",
                        "/suppliers/**"
                )
                .build();
    }
}
