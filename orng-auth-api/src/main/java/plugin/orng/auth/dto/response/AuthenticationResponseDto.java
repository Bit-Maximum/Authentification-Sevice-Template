package plugin.orng.auth.dto.response;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Schema(description = "Ответ аутентификации")
public class AuthenticationResponseDto {

    @Schema(description = "Access токен", example = "MySuperSecretAndVeryImportantToken")
    @NotBlank(message = "Access Token обязателен")
    private String accessToken;

    @Schema(description = "Refresh токен", example = "YouWillNewerKnow")
    @NotBlank(message = "Refresh Token обязателен")
    private String refreshToken;
}
