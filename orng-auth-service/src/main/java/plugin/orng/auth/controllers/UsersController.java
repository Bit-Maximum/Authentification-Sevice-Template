package plugin.orng.auth.controllers;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import plugin.orng.auth.api.*;
import plugin.orng.auth.dto.request.*;
import plugin.orng.auth.dto.response.*;
import plugin.orng.auth.service.base.*;

@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

    private final UserService userService;

    /**
     * Регистрация нового пользователя.
     *
     * @param request данные для регистрации
     *
     * @return ответ о результате регистрации
     */
    @Override
    public ResponseEntity<String> registration(
        RegistrationRequestDto request
    ) {
        userService.register(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
