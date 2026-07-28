package ism.gnims.coutcyclevie.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API CCV")
                        .version("1.0")
                        .description("Documentation du endpoint pour le calcul du cout du cycle de vie.")
                        .contact(new Contact()
                                .name("Maxime Gnimdou SOH")
                                .email("gnimdou.soh.ppep@gmail.com")));
    }

}
