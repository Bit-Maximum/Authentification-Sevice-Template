package plugin.orng.auth.filter;

import java.io.*;
import java.util.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

import plugin.orng.auth.*;
import plugin.orng.auth.utils.*;
import plugin.orng.auth.validator.*;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtAccessTokenValidator validator;

    private final JwtParser jwtParser;

    /**
     * Фильтр JWT, который проверяет JWT-токен в заголовке Authorization
     * и устанавливает аутентификацию пользователя, если токен валиден.
     */
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader(JwtTokenExtractor.AUTH_HEADER);

        if (header == null || !header.startsWith(JwtTokenExtractor.BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(JwtTokenExtractor.BEARER.length());

        if (!validator.isValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Если уже авторизован, то пропускаем фильтр
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        Principal principal = new Principal(jwtParser.extractUserId(token));
        List<SimpleGrantedAuthority> authorities = jwtParser.extractAuthorities(token);

        log.trace("-----------------------------------");
        log.trace(jwtParser.extractAuthorities(token).toString());
        log.trace(authorities.toString());
        log.trace("-----------------------------------");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            principal,
            null,
            authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

}
