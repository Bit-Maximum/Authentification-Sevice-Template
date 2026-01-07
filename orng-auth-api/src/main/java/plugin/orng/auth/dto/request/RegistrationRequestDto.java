package plugin.orng.auth.dto.request;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Schema(description = "Запрос на регистрацию")
public class RegistrationRequestDto {

    @Schema(description = "Логин пользователя", example = "Bit Maximum")
    @Size(max = 128, message = "Имя пользователя должно содержать до 128 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @Schema(description = "Пароль пользователя", example = "my_1secret1_password")
    @Size(min = 8, max = 255, message = "Пароль пользователя должно содержать от 8 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

}
