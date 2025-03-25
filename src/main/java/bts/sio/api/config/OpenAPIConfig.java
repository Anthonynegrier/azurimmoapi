package bts.sio.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://172.20.177.251:8080/anthonyazurimmo").description("Development Server"),
                        new Server().url("https://prodtomcat.inforostand14.net/anthonyazurimmo").description("Production Server")
                ))
                .info(new Info()
                        .title("Anthony AzurImmo API")
                        .version("1.0")
                        .description("API for managing real estate properties, apartments, contracts, and payments")
                        .contact(new Contact()
                                .name("Anthony AzurImmo Support")
                                .email("support@anthonyazurimmo.com")
                        )
                );
    }
}