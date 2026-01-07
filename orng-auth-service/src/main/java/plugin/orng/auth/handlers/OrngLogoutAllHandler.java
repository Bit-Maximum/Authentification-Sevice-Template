package plugin.orng.auth.handlers;

import java.time.*;

import jakarta.servlet.http.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;

import org.springframework.security.web.authentication.logout.*;

import plugin.orng.auth.*;
import plugin.orng.auth.repository.*;
import plugin.orng.auth.utils.*;
import plugin.orng.auth.validator.*;

@Component
@RequiredArgsConstructor
public class OrngLogoutAllHandler implements LogoutHandler {

    private final TokensRepository tokensRepository;

    private final JwtParser jwtParser;

    private final JwtAccessTokenValidator validator;

    @Override
    public void logout(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) {
        String token = JwtTokenExtractor.extractToken(request);

        if (!validator.isValid(token)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        Long userId = jwtParser.extractUserId(token);
        tokensRepository.softDeleteByUserId(userId, Instant.now());

        response.setStatus(HttpStatus.NO_CONTENT.value());

    }

}
