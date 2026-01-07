package plugin.orng.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;
import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "users")
@RequiredArgsConstructor
public class UserEntity implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(max = 255, message = "Имя пользователя может содержать до 255 символов")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 255, message = "Пароль должен содержать от 8 до 255 символов")
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<TokenEntity> tokens;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<RoleEntity> roleEntities;

    /*
     * Переопределение методов UserDetails
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleEntities != null ? roleEntities : List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
