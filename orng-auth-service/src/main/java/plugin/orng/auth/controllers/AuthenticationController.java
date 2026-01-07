package plugin.orng.auth.controllers;

import jakarta.servlet.http.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import plugin.orng.auth.api.*;
import plugin.orng.auth.dto.request.*;
import plugin.orng.auth.dto.response.*;
import plugin.orng.auth.handlers.*;
import plugin.orng.auth.service.base.*;

@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthApi {

    private final AuthenticationService authenticationService;

    /**
     * Вход в аккаунт существующего пользователя
     *
     * @param request данные для входа в аккаунт
     * @return ответ с парой токенов доступа к системе
     */
    @Override
    public ResponseEntity<AuthenticationResponseDto> login(LoginRequestDto request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    /**
     * Ротация активной пары токенов.
     *
     * @param token HTTP запрос с токеном обновления в заголовках
     * @return ответ с новой парой токенов доступа к системе
     */
    @Override
    public ResponseEntity<AuthenticationResponseDto> refresh(String token) {
        return authenticationService.refreshToken(token);
    }

    /**
     * Выход со всех устройств.
     *
     * @param authHeader HTTP запрос с токеном обновления в заголовках
     */
    @Override
    public ResponseEntity<String> logoutAll(String authHeader) {
        authenticationService.logoutAll(authHeader);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    /**
     * Завершение текущей сессии.
     *
     * @param authHeader HTTP запрос с токеном обновления в заголовках
     */
    @Override
    public ResponseEntity<String> logout(String authHeader) {
        authenticationService.logout(authHeader);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
