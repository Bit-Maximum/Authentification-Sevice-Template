package plugin.orng.auth.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {

    public static final String SECURITY_SCHEME_NAME = "Bearer Authorization";

    @Bean
    public OpenAPI customOpenApi(
        @Value("${spring.application.name}") String appName,
        @Value("${spring.application.description}") String appDescription,
        @Value("${spring.application.version}") String appVersion
    ) {
        return new OpenAPI().info(new Info()
                .title(appName)
                .version(appVersion)
                .description(appDescription))
            .addSecurityItem(new SecurityRequirement()
                .addList(SECURITY_SCHEME_NAME))
            .components(new Components()
                .addSecuritySchemes(
                    SECURITY_SCHEME_NAME, new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }

}
