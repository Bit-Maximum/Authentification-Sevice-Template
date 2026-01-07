package plugin.orng.auth.config;

import lombok.*;
import org.springframework.boot.context.properties.*;
import org.springframework.validation.annotation.*;

@Getter
@Validated
@AllArgsConstructor
@ConfigurationProperties(prefix = "security.jwt.signing")
public class JwtSigningProperties {

    /**
     * Приватный ключ для подписи токенов
     */
    private String privateKey;

    /**
     * Время жизни токенов доступа.
     * По умолчанию: 5 минут
     */
    private long accessTokenExpirationSeconds = 300;

    /**
     * Время жизни токенов обновления.
     * По умолчанию: 1 неделя
     */
    private long refreshTokenExpirationSeconds = 604800;

    /**
     * Название издателя издаваемых токенов
     */
    private String jwtIssuer = "orng-auth";

    /**
     * Название роли выдаваеммой новым пользователям по умолчанию.
     * Должно совпадать с названием роли в базе данных.
     */
    private String defaultUserRole;
}
