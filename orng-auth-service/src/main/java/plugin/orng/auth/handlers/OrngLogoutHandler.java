package plugin.orng.auth.handlers;

import java.time.*;
import java.util.*;

import jakarta.servlet.http.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.logout.*;
import org.springframework.stereotype.*;

import plugin.orng.auth.*;
import plugin.orng.auth.repository.*;
import plugin.orng.auth.utils.*;
import plugin.orng.auth.validator.*;

@Component
@RequiredArgsConstructor
public class OrngLogoutHandler implements LogoutHandler {

    private final TokensRepository tokensRepository;

    private final JwtParser jwtParser;

    private final JwtAccessTokenValidator validator;

    /**
     * Обработчик выхода пользователя из системы.
     * Устанавливает флаг "loggedOut" в true для соответствующего токена,
     * если токен найден в хранилище.
     *
     * @param request HttpServletRequest объект, содержащий информацию о запросе.
     * @param response HttpServletResponse объект, содержащий информацию об ответе.
     * @param authentication объект аутентификации.
     */
    @Override
    public void logout(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) {
        String token = JwtTokenExtractor.extractToken(request);

        if (!validator.isValid(token)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        UUID accessJti = jwtParser.extractJti(token);
        tokensRepository.softDeleteByAccessJti(accessJti, Instant.now());

        response.setStatus(HttpStatus.NO_CONTENT.value());
    }

}
