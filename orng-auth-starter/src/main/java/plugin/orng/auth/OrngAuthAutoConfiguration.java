package plugin.orng.auth;

import java.util.*;

import javax.swing.plaf.*;

import jakarta.annotation.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.security.servlet.*;
import org.springframework.boot.autoconfigure.web.servlet.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.*;
import org.springframework.http.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.*;
import org.springframework.security.web.access.*;
import org.springframework.security.web.authentication.*;
import org.springframework.web.cors.*;

import plugin.orng.auth.filter.*;
import plugin.orng.auth.handler.*;
import plugin.orng.auth.utils.*;
import plugin.orng.auth.validator.*;

@Slf4j
@AutoConfiguration
@EnableMethodSecurity
@EnableConfigurationProperties(JwtVerificationProperties.class)
public class OrngAuthAutoConfiguration {

    @PostConstruct
    public void init() {
        log.info("orng-auth-starter initialised!");
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtVerificationKeyProvider jwtVerificationKeyProvider(JwtVerificationProperties properties) {
        return new JwtVerificationKeyProvider(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtParser jwtParser(JwtVerificationKeyProvider keyProvider) {
        return new JwtParser(keyProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtValidator jwtValidator(JwtParser jwtParser) {
        return new JwtValidator(jwtParser);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAccessTokenValidator jwtAccessTokenValidator(
        JwtVerificationProperties properties,
        JwtParser jwtParser,
        JwtValidator jwtValidator
    ) {
        return new JwtAccessTokenValidator(properties, jwtParser, jwtValidator);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtFilter jwtFilter(JwtAccessTokenValidator validator, JwtParser jwtParser) {
        return new JwtFilter(validator, jwtParser);
    }

    @Bean
    @ConditionalOnMissingBean
    AccessDeniedHandler accessDeniedHandler() {
        return new OrngAccessDeniedHandler();
    }

    //    @Bean
    //    @ConditionalOnMissingBean
    //    CorsConfigurationSource corsConfigurationSource() {
    //        CorsConfiguration configuration = new CorsConfiguration();
    //        configuration.setAllowedOrigins(List.of("*"));
    //        configuration.setAllowedMethods(List.of("*"));
    //        configuration.setAllowedHeaders(List.of("*"));
    //        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //        source.registerCorsConfiguration("/**", configuration);
    //        return source;
    //    }

    //    @Bean
    //    @ConditionalOnMissingBean
    //    UserDetailsService userDetailsService() {
    //        return username -> {
    //            throw new UsernameNotFoundException("JWT-only authentication");
    //        };
    //    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JwtFilter jwtFilter,
        JwtVerificationProperties properties
    ) throws Exception {

        log.error("SETTING STARTER FILTER CHAIN");

        //        http.cors(cors ->
        //            cors.configurationSource(corsConfigurationSource()));

        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(properties.getAllowedUrls().toArray(String[]::new)).permitAll();
            auth.anyRequest().permitAll();
        });

        http.exceptionHandling(ex -> {
            ex.accessDeniedHandler(accessDeniedHandler());
            ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        });

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
