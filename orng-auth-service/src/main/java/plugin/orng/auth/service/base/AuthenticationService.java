package plugin.orng.auth.service.base;

import jakarta.servlet.http.*;
import org.springframework.http.*;

import plugin.orng.auth.dto.request.*;
import plugin.orng.auth.dto.response.*;

public interface AuthenticationService {

    AuthenticationResponseDto authenticate(LoginRequestDto request);

    ResponseEntity<AuthenticationResponseDto> refreshToken(String authHeader);

    void logoutAll(String authHeader);

    void logout(String authHeader);

}
