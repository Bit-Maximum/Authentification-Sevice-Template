package plugin.orng.auth.config;

import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

@Configuration
@EnableConfigurationProperties(JwtSigningProperties.class)
public class JwtConfig {

}
