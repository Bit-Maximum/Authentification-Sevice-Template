package plugin.orng.auth.jwt;

import java.time.*;
import java.util.*;

import lombok.*;
import org.springframework.stereotype.*;

import plugin.orng.auth.*;
import plugin.orng.auth.config.*;
import plugin.orng.auth.entity.*;
import plugin.orng.auth.repository.*;
import plugin.orng.auth.utils.*;
import plugin.orng.auth.validator.*;

@Service
@RequiredArgsConstructor
public class JwtRefreshTokenValidator {

    private final JwtSigningProperties properties;

    private final TokensRepository tokenRepository;

    private final JwtParser jwtParser;

    private final JwtValidator jwtValidator;

    /**
     * Проверяет, является ли токен обновления JWT действительным для указанного пользователя.
     *
     * @param token Токен обновления для проверки
     * @param user Данные пользователя для сравнения
     *
     * @return true, если токен обновления действителен для пользователя, в противном случае false
     */
    public boolean isValid(String token, UserEntity user) {

        if (!JwtTypes.REFRESH.equals(jwtParser.extractTokenType(token))) {
            return false;
        }

        if (!jwtParser.extractIssuer(token).equals(properties.getJwtIssuer())) {
            return false;
        }

        if (jwtValidator.isTokenExpired(token)) {
            return false;
        }

        UUID refreshJti = jwtParser.extractJti(token);

        return tokenRepository.findByRefreshJti(refreshJti)
            .filter(t -> t.getExpiredAt().isAfter(Instant.now()))
            .filter(t -> t.getUser().getId().equals(user.getId()))
            .isPresent();
    }

}
