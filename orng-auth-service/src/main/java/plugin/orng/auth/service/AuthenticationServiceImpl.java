package plugin.orng.auth.service;

import java.time.*;
import java.util.*;

import lombok.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import plugin.orng.auth.*;
import plugin.orng.auth.config.*;
import plugin.orng.auth.dto.request.*;
import plugin.orng.auth.dto.response.*;
import plugin.orng.auth.entity.*;
import plugin.orng.auth.jwt.*;
import plugin.orng.auth.repository.*;
import plugin.orng.auth.service.base.*;
import plugin.orng.auth.utils.*;
import plugin.orng.auth.validator.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtSigningProperties properties;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final UsersRepository usersRepository;

    private final TokensRepository tokensRepository;

    private final JwtParser jwtParser;

    private final JwtRefreshTokenValidator refreshTokenValidator;

    private final JwtAccessTokenValidator accessTokenValidator;

    /**
     * Авторизация пользователя.
     *
     * @param request объект с данными пользователя для авторизации
     *
     * @return объект с токенами авторизации
     */
    @Override
    @Transactional
    public AuthenticationResponseDto authenticate(LoginRequestDto request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        UserEntity user = usersRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException(
                "Пользователь с именем " + request.getUsername() + " не найден."));

        UUID accessJti = UUID.randomUUID();
        UUID refreshJti = UUID.randomUUID();
        String accessToken = jwtProvider.generateAccessToken(user, accessJti);
        String refreshToken = jwtProvider.generateRefreshToken(user, refreshJti);

        saveUserToken(accessJti, refreshJti, user);

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }

    /**
     * Обновляет токенов аутентификации.
     *
     * @param authHeader HTTP-запрос, содержащий токен обновления.
     *
     * @return Ответ с обновленным токеном.
     */
    @Override
    @Transactional
    public ResponseEntity<AuthenticationResponseDto> refreshToken(String authHeader) {

        // Извлечёт токен из заголовка
        String token = JwtTokenExtractor.extractToken(authHeader);

        // Проверит валидность токена и вернёт Claim из токена, токен нормальный
        Long userId = jwtParser.extractUserId(token);

        UserEntity user = usersRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException(
                "Пользователь с ID " + userId + " не найден."));

        if (refreshTokenValidator.isValid(token, user)) {

            UUID accessJti = UUID.randomUUID();
            UUID refreshJti = UUID.randomUUID();
            String accessToken = jwtProvider.generateAccessToken(user, accessJti);
            String refreshToken = jwtProvider.generateRefreshToken(user, refreshJti);

            tokensRepository.softDeleteByRefreshJti(jwtParser.extractJti(token), Instant.now());

            saveUserToken(accessJti, refreshJti, user);

            return new ResponseEntity<>(
                new AuthenticationResponseDto(accessToken, refreshToken),
                HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Метод отзывает все действительные токены для данного пользователя.
     *
     * @param authHeader HTTP-запрос, содержащий токен доступа.
     */
    @Override
    @Transactional
    public void logoutAll(String authHeader) {
        String token = JwtTokenExtractor.extractToken(authHeader);

        if (!accessTokenValidator.isValid(token)) {
            throw new RuntimeException("Токен не корректный");
        }

        Long userId = jwtParser.extractUserId(token);
        tokensRepository.softDeleteByUserId(userId, Instant.now());
    }

    /**
     * Метод отзывает токен обновления связанный с переданным токеном доступа.
     *
     * @param authHeader HTTP-запрос, содержащий токен доступа.
     */
    @Override
    @Transactional
    public void logout(String authHeader) {
        String token = JwtTokenExtractor.extractToken(authHeader);

        if (!accessTokenValidator.isValid(token)) {
            throw new RuntimeException("Токен не корректный");
        }

        UUID accessJti = jwtParser.extractJti(token);
        tokensRepository.softDeleteByAccessJti(accessJti, Instant.now());
    }

    /**
     * Сохраняет токен авторизации пользователя в базе данных.
     *
     * @param accessJti ID Токена авторизации.
     * @param refreshJti ID Токена обновления.
     * @param user Информация о пользователе.
     */
    private void saveUserToken(UUID accessJti, UUID refreshJti, UserEntity user) {

        Instant now = Instant.now();
        TokenEntity token = new TokenEntity();

        token.setUser(user);
        token.setAccessJti(accessJti);
        token.setRefreshJti(refreshJti);
        token.setIssuedAt(now);
        token.setExpiredAt(now.plusSeconds(properties.getRefreshTokenExpirationSeconds()));

        tokensRepository.save(token);
    }

}
