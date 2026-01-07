package plugin.orng.auth.service;

import java.util.*;

import lombok.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import plugin.orng.auth.config.*;
import plugin.orng.auth.dto.request.*;
import plugin.orng.auth.dto.response.*;
import plugin.orng.auth.entity.*;
import plugin.orng.auth.repository.*;
import plugin.orng.auth.service.base.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtSigningProperties properties;

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Метод загружает пользователя по его имени. Если пользователь не найден, выбрасывает
     * исключение UsernameNotFoundException.
     *
     * @param username имя пользователя для поиска
     * @return найденный пользователь
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository
                .findByUsername(username)
                .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Пользователь с именем" + username + " не найден"));
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param request запрос на регистрацию
     *
     */
    public void register(RegistrationRequestDto request) {

        if (existsByUsername(request.getUsername())) {
            throw new RuntimeException("Имя пользователя уже занято");
        }

        UserEntity user = new UserEntity();

        RoleEntity defaultRole = rolesRepository
            .findByCode(properties.getDefaultUserRole())
            .orElseThrow(() ->
                new IllegalStateException(
                    "Default role " + properties.getDefaultUserRole() + " not found"));

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoleEntities(Set.of(defaultRole));
        user.setEnabled(true);

        usersRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        UserEntity user = usersRepository.findByUsername(username).orElse(null);
        return user != null;
    }
}
