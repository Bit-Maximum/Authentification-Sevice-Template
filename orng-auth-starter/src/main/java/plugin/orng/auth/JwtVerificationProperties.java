package plugin.orng.auth;

import java.util.*;

import lombok.*;
import org.springframework.boot.context.properties.*;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.jwt.verification")
public class JwtVerificationProperties {

    /**
     * Публичный ключ для проверки подписи токенов доступа
     */
    private String publicKey;

    /**
     * Название издателя получаемых токенов доступа.
     * Должно совпадать с названием вашего издателя токенов.
     */
    private String jwtIssuer = "orng-auth";

    private List<String> allowedUrls = List.of("/swagger-ui/**", "/v3/**", "/registration");

}
