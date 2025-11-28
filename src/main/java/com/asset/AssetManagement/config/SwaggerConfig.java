package com.asset.AssetManagement.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Asset Management API")
                .description("API documentation for Asset Management System"))
                .servers(List.of(new Server().url("http://localhost:8080").description("Local server"),
                                 new Server().url("https://localhost:8081").description("Live server")));
    }
}
