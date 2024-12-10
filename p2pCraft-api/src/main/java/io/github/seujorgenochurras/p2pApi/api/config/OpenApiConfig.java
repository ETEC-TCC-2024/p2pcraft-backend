package io.github.seujorgenochurras.p2pApi.api.config;

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

    @Value("p2pApi.openApi.dev.url")
    private String devUrlDomain;

    @Bean
    public OpenAPI customOpenApi() {
        Server devServer = new Server();
        devServer.setUrl(devUrlDomain);
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setName("JorgeDev");
        contact.setUrl("https://IDontHaveAwebsiteYet.sad");
        contact.setEmail("little_jhey@outlook.com");

        License mitLicense = new License().name("MIT License")
            .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info().title("P2PCraft API")
            .version("1.0")
            .contact(contact)
            .description("This is the oficial P2PCraft Web API ")
            .license(mitLicense);

        return new OpenAPI().info(info)
            .servers(List.of(devServer));
    }

}
