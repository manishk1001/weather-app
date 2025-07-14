package weatherApp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Weather Info API")
                        .version("1.0.0")
                        .description("A RESTful API to fetch weather information based on Indian pincodes and dates.")
                        .contact(new Contact()
                                .name("Manish Kumar")
                                .email("k.manish9973@gmail.com")
                                .url("https://github.com/manishk1001")
                        )
                );
    }
}
