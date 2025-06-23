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
    @Value("${PICKDROP_API_SERVER_URL:http://localhost:8080}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        Server mainServer = new Server();
        mainServer.setUrl(serverUrl);
        mainServer.setDescription("Serveur Principal");

        return new OpenAPI()
                .servers(List.of(mainServer))
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