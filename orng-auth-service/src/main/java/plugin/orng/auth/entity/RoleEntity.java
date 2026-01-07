package plugin.orng.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity(name = "roles")
@RequiredArgsConstructor
public class RoleEntity implements GrantedAuthority {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true)
    @NotBlank(message = "Код роли не может быть пустым")
    @Size(max = 128, message = "Код роли может быть длиной до 128 символов")
    private String code;

    public String getAuthority() {
        return code;
    }
}
