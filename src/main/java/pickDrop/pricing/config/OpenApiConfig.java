package pickDrop.pricing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    // Injecte la valeur de l'URL du serveur depuis application.properties ou l'environnement
    @Value("${pickdrop.api.server.url:http://localhost:8080}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        // Le serveur est maintenant dynamique. En production, il prendra l'URL de Render.
        Server productionServer = new Server();
        productionServer.setUrl(serverUrl);
        productionServer.setDescription("Serveur Principal");

        return new OpenAPI()
                .servers(List.of(productionServer)) // On ne garde que le serveur pertinent
                .info(new Info()
                        .title("Pick&Drop Pricing API")
                        .description("API de calcul de tarification pour le système Pick&Drop.")
                        .version("2.1.0")
                        .contact(new Contact()
                                .name("Équipe Backend Pick&Drop")
                                .email("backend@pickanddrop.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}