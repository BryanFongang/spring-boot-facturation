package com.camerexpress.pricing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Serveur de développement"),
                        new Server().url("https://api.Pick&Drop.com").description("Serveur de production")
                ))
                .info(new Info()
                        .title("Pick&Drop Pricing API")
                        .description("API de calcul de tarification pour le système Pick&Drop basée sur le modèle mathématique complet")
                        .version("2.1.0")
                        .contact(new Contact()
                                .name("Équipe Backend Pick&Drop")
                                .email("bryanofongang.gmail.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}