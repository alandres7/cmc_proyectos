package es.grupocmc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${application.title}") String appTitle,
            @Value("${application.description}") String appDescription,
            @Value("${application.version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title(appTitle)
                        .description(appDescription)
                        .version(appVersion)
                        .license(new License().name("Apache 2.0")
                                .url("https://www.grupocmc.es/servicios/application-services/")));
    }
}
