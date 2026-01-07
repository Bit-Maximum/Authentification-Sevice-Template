package plugin.orng.auth.validator;

import lombok.*;

import plugin.orng.auth.*;
import plugin.orng.auth.utils.*;

@RequiredArgsConstructor
public class JwtAccessTokenValidator {

    private final JwtVerificationProperties properties;

    private final JwtParser jwtParser;

    private final JwtValidator jwtValidator;

    /**
     * Проверяет, является ли токен доступа действительным.
     *
     * @param token Токен для проверки
     * @return true, если токен действителен, в противном случае false
     */
    public boolean isValid(String token) {

        if (!JwtTypes.ACCESS.equals(jwtParser.extractTokenType(token))) {
            return false;
        }

        if (!jwtParser.extractIssuer(token).equals(properties.getJwtIssuer())) {
            return false;
        }

        return !jwtValidator.isTokenExpired(token);
    }

}
