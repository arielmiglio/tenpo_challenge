package cl.tenpo.challenge.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ariel Miglio
 * @date 24/8/2021
 */

@Component
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components().
                                addSecuritySchemes("Bearer-jwt",
                                                    new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                                    .in(SecurityScheme.In.HEADER).name("Authorization")
                                )
                        )
                .info(getInfo())
                .addSecurityItem(
                        new SecurityRequirement().addList("Bearer-jwt", Arrays.asList("read", "write")));
    }

    private Info getInfo() {
        Contact contact = new Contact();
        contact.setName("Ariel Miglio");
        contact.setEmail("arielmiglio@gmail.com");
        return new Info()
                .title("Tenpo Challenge")
                .description("API Swagger para testear  los request del challenge" +
                        "Completar " +
                        "Completar " +
                        "Anteponer el prefijo  \"Bearer \".")
                .version("1.0.0")
                .contact(contact);
    }

}
