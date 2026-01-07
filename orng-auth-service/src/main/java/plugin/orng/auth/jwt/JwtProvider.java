package plugin.orng.auth.jwt;

import io.jsonwebtoken.*;
import java.time.*;
import java.util.*;
import lombok.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;

import plugin.orng.auth.*;
import plugin.orng.auth.config.*;
import plugin.orng.auth.entity.*;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtSigningProperties properties;

    private final JwtSigningKeyProvider keyProvider;

    /**
     * Генерирует токен доступа JWT для пользователя.
     *
     * @param userEntity пользователь, для которого генерируется токен
     * @return сгенерированный токен доступа JWT
     */
    public String generateAccessToken(UserEntity userEntity, UUID accessJti) {
        Instant now = Instant.now();
        JwtBuilder builder =
                Jwts.builder()
                        .id(accessJti.toString())
                        .subject(userEntity.getUsername())
                        .issuer(properties.getJwtIssuer())
                        .claim("user_id", userEntity.getId())
                        .claim("token_type", JwtTypes.ACCESS)
                        .claim(
                                "authorities",
                                userEntity.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList())
                        .issuedAt(Date.from(now))
                        .expiration(
                                Date.from(
                                        now.plusSeconds(
                                                properties.getAccessTokenExpirationSeconds())))
                        .signWith(keyProvider.getPrivateKey());
        return builder.compact();
    }

    /**
     * Генерирует токен обновления JWT для пользователя.
     *
     * @param userEntity пользователь, для которого генерируется токен обновления
     * @return сгенерированный токен обновления JWT
     */
    public String generateRefreshToken(UserEntity userEntity, UUID refreshJti) {
        Instant now = Instant.now();
        JwtBuilder builder =
                Jwts.builder()
                        .id(refreshJti.toString())
                        .subject(userEntity.getUsername())
                        .issuer(properties.getJwtIssuer())
                        .claim("user_id", userEntity.getId())
                        .claim("token_type", JwtTypes.REFRESH)
                        .issuedAt(Date.from(now))
                        .expiration(
                                Date.from(
                                        now.plusSeconds(
                                                properties.getRefreshTokenExpirationSeconds())))
                        .signWith(keyProvider.getPrivateKey());
        return builder.compact();
    }
}
