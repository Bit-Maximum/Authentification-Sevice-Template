package plugin.orng.auth.api;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import plugin.orng.auth.dto.request.*;

@Tag(name = "Пользователи")
public interface UsersApi {

    @PostMapping("/auth/registration")
    @Operation(summary = "Регистрация пользователя")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Успешная регистрация пользователя"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Попытка повторной регистрации",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class),
                examples = @ExampleObject(value = """
                    {
                        "type": "about:blank",
                        "title": "Registration duplicate",
                        "status": 409,
                        "detail": "Пользователь с таким именем уже зарегистрирован.",
                        "instance": "/auth/registration"
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Почта или пароль не прошли валидацию",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class),
                examples = @ExampleObject(value = """
                    {
                        "type": "about:blank",
                        "title": "Validation error",
                        "status": 400,
                        "detail": "Некорректный формат полей.",
                        "instance": "/auth/registration",
                        "errors": {
                            "username": "Имя пользователя должно содержать до 128 символов",
                            "password": "Пароль пользователя должно содержать от 8 до 255 символов"
                        }
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Внутренняя ошибка сервера",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class),
                examples = @ExampleObject(value = """
                    {
                        "type": "about:blank",
                        "title": "Internal server error",
                        "status": 500,
                        "detail": "Внутрення ошибка сервера. Попробуйте позже.",
                        "instance": "/auth/registration"
                    }
                    """)
            )
        )
    })
    ResponseEntity<String> registration(
        @Valid @RequestBody RegistrationRequestDto registrationDto
    );

}
