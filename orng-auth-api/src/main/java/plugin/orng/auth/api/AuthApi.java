package plugin.orng.auth.api;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import plugin.orng.auth.dto.request.*;
import plugin.orng.auth.dto.response.*;

@Tag(name = "Авторизация")
public interface AuthApi {

    @PostMapping("/auth/login")
    @Operation(summary = "Аутентификация пользователя")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Выпуск пары токенов",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthenticationResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Неверный логин или пароль",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class),
                examples = @ExampleObject(value = """
                    {
                        "type": "about:blank",
                        "title": "Invalid credentials",
                        "status": 404,
                        "detail": "Неверный логин или пароль.",
                        "instance": "/auth/login"
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
                        "instance": "/auth/login",
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
                        "instance": "/auth/login"
                    }
                    """)
            )
        )
    })
    ResponseEntity<AuthenticationResponseDto> login(@Valid @RequestBody LoginRequestDto request);

    @PostMapping("/auth/refresh")
    @Operation(summary = "Обновление пары JWT токенов")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Пара токенов обновлена",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthenticationResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Пользователь не аутентифицирован",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class),
                examples = @ExampleObject(value = """
                    {
                        "type": "about:blank",
                        "title": "Missing token",
                        "status": 401,
                        "detail": "Bearer Token не найден в заголовке Authorization.",
                        "instance": "/auth/refresh"
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Невалидный токен",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class),
                examples = @ExampleObject(value = """
                    {
                        "type": "about:blank",
                        "title": "Invalid token",
                        "status": 401,
                        "detail": "Токен просрочен или неверного формата.",
                        "instance": "/auth/refresh"
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
                        "instance": "/auth/refresh"
                    }
                    """)
            )
        )
    })
    ResponseEntity<AuthenticationResponseDto> refresh(
        @Parameter(
            description = "Токен обновления в формате: Bearer <refresh_token>",
            required = true,
            example = "Bearer eyJhbGciOiJI.UzI1NiIsInR5cCI6.IkpXVCJ9",
            schema = @Schema(type = "string")
        )
        @RequestHeader("Authorization") String authHeader);

    @PostMapping("/auth/logout/all")
    @Operation(summary = "Выход со всех устройств")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Успешный выход со всех устройств"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Пользователь не аутентифицирован",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class),
                examples = @ExampleObject(value = """
                    {
                        "type": "about:blank",
                        "title": "Missing token",
                        "status": 401,
                        "detail": "Bearer Token не найден в заголовке Authorization.",
                        "instance": "/auth/logout/all"
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Невалидный токен",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class),
                examples = @ExampleObject(value = """
                    {
                        "type": "about:blank",
                        "title": "Invalid token",
                        "status": 401,
                        "detail": "Токен просрочен или неверного формата.",
                        "instance": "/auth/logout/all"
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
                        "instance": "/auth/logout/all"
                    }
                    """)
            )
        )
    })
    ResponseEntity<String> logoutAll(
        @Parameter(
            description = "Токен авторизации в формате: Bearer <access_token>",
            required = true,
            example = "Bearer eyJhbGciOiJI.UzI1NiIsInR5cCI6.IkpXVCJ9",
            schema = @Schema(type = "string")
        )
        @RequestHeader("Authorization") String authHeader);

    @PostMapping("/auth/logout")
    @Operation(summary = "Завершение текущей сессии")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Успешное завершение текущей сессии",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthenticationResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Пользователь не аутентифицирован",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class),
                examples = @ExampleObject(value = """
                    {
                        "type": "about:blank",
                        "title": "Missing token",
                        "status": 401,
                        "detail": "Bearer Token не найден в заголовке Authorization.",
                        "instance": "/auth/logout"
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Невалидный токен",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProblemDetail.class),
                examples = @ExampleObject(value = """
                    {
                        "type": "about:blank",
                        "title": "Invalid token",
                        "status": 401,
                        "detail": "Токен просрочен или неверного формата.",
                        "instance": "/auth/logout"
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
                        "instance": "/auth/logout"
                    }
                    """)
            )
        )
    })
    ResponseEntity<String> logout(
        @Parameter(
            description = "Токен авторизации в формате: Bearer <access_token>",
            required = true,
            example = "Bearer eyJhbGciOiJI.UzI1NiIsInR5cCI6.IkpXVCJ9",
            schema = @Schema(type = "string")
        )
        @RequestHeader("Authorization") String authHeader);

}
