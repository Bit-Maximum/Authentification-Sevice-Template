package plugin.orng.auth.config;

import java.util.*;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.core.context.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;
import org.springframework.web.cors.*;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import plugin.orng.auth.filter.*;
import plugin.orng.auth.handler.*;
import plugin.orng.auth.handlers.*;
import plugin.orng.auth.service.base.*;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    private final String[] allowedUrls = new String[]{
        "/swagger-ui/**",
        "/v3/**",
        "/login",
        "/refresh",
        "/registration"
    };

    /**
     * Метод создает и настраивает цепочку фильтров безопасности для HTTP.
     *
     * @param http объект HttpSecurity для настройки цепочки фильтров безопасности
     *
     * @return созданная цепочка фильтров безопасности
     */
    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        UserService userService
    ) throws Exception {

        log.error("SETTING AUTH SERVICE FILTER CHAIN");

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(allowedUrls).permitAll();
            auth.anyRequest().permitAll();
        });

        http.userDetailsService(userService);
        http.exceptionHandling(ex ->
            ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        http.sessionManagement(session ->
            session.sessionCreationPolicy(STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
