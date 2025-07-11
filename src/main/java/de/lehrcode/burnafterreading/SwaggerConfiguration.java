package de.lehrcode.burnafterreading;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.util.Optional;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI openAPI(Optional<BuildProperties> buildPropOpt) {
        return new OpenAPI()
            .info(buildPropOpt.map(buildProperties -> new Info().title(buildProperties.getName())
                                                                .version(buildProperties.getVersion()))
                              .orElse(null));
    }
}
