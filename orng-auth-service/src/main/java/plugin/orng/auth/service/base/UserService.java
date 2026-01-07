package plugin.orng.auth.service.base;

import org.springframework.security.core.userdetails.*;

import plugin.orng.auth.dto.request.*;
import plugin.orng.auth.dto.response.*;

public interface UserService extends UserDetailsService {

    boolean existsByUsername(String username);

    void register(RegistrationRequestDto request);

}
