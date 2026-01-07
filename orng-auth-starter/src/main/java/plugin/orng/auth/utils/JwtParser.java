package plugin.orng.auth.utils;

import io.jsonwebtoken.*;

import java.time.*;
import java.util.*;
import java.util.function.*;

import lombok.*;
import org.springframework.security.core.authority.*;

import plugin.orng.auth.*;

@RequiredArgsConstructor
public class JwtParser {

    private final JwtVerificationKeyProvider keyProvider;

    /**
     * Извлекает момент истечения из токена.
     *
     * @param token Токен для извлечения момента истечения
     *
     * @return Момент истечения, извлеченный из токена
     */
    public Instant extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration).toInstant();
    }

    /**
     * Извлекает название сервиса, выпустившего токен, из токена.
     *
     * @param token Токен для извлечения выпустившего его сервиса
     *
     * @return название сервиса, выпустившего токен, извлеченное из токена
     */
    public String extractIssuer(String token) {
        return extractClaim(token, Claims::getIssuer);
    }

    /**
     * Извлекает имя пользователя из токена.
     *
     * @param token Токен для извлечения имени пользователя
     *
     * @return Имя пользователя, извлеченное из токена
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает JTI из токена.
     *
     * @param token Токен для извлечения JTI
     *
     * @return JTI, извлеченное из токена
     */
    public UUID extractJti(String token) {
        return extractClaim(token, claims -> UUID.fromString(claims.getId()));
    }

    /**
     * Извлекает ID пользователя из токена.
     *
     * @param token Токен для извлечения ID пользователя
     *
     * @return ID пользователя, извлеченное из токена
     */
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("user_id", Long.class));
    }

    /**
     * Извлекает тип токена из токена.
     *
     * @param token Токен для извлечения типа токена
     *
     * @return Тип токена, извлеченное из токена
     */
    public JwtTypes extractTokenType(String token) {
        return extractClaim(
            token,
            claims -> JwtTypes.valueOf(claims.get("token_type", String.class)));
    }

    /**
     * Извлекает список прав доступа пользователя из токена.
     *
     * @param token Токен для извлечения списка прав доступа
     *
     * @return список прав доступа пользователя, извлеченные из токена
     */
    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        List<?> authorities = extractClaim(token,
            claims -> claims.get("authorities", List.class));

        if (authorities == null) {
            return Collections.emptyList();
        }

        return authorities.stream()
            .filter(String.class::isInstance)
            .map(authority -> new SimpleGrantedAuthority((String) authority))
            .toList();
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Извлекает все утверждения из указанного токена.
     *
     * @param token токен, из которого нужно извлечь утверждения
     *
     * @return объект Claims, содержащий все утверждения
     */
    private Claims extractAllClaims(String token) {
        JwtParserBuilder parser = Jwts.parser();
        parser.verifyWith(keyProvider.getPublicKey());
        return parser.build().parseSignedClaims(token).getPayload();
    }

}
