package plugin.orng.auth.validator;

import java.time.*;

import lombok.*;

import plugin.orng.auth.utils.*;

@RequiredArgsConstructor
public class JwtValidator {

    private final JwtParser jwtParser;

    /**
     * Проверяет, истек ли срок действия токена.
     *
     * @param token Токен для проверки
     * @return true, если срок действия токена истек, в противном случае false
     */
    public boolean isTokenExpired(String token) {
        return jwtParser.extractExpiration(token).isBefore(Instant.now());
    }
}
