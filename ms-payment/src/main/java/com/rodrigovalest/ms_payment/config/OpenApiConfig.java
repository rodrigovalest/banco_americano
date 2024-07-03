package com.rodrigovalest.ms_payment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-payment")
                        .description("payment microservice")
                        .version("v1")
                        .license(new License().name("Apache").url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .contact(new Contact().name("Rodrigo do Vale Stankowicz"))
                );
    }
}
