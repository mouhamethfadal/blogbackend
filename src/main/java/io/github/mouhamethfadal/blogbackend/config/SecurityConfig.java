package io.github.mouhamethfadal.blogbackend.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Environment environment;

    private static final String DEV_PROFILE = "dev";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String activeProfile = Arrays.stream(environment.getActiveProfiles()).findFirst().orElse(DEV_PROFILE);

        configureCommonSecurity(http);

        if (DEV_PROFILE.equals(activeProfile)) {
            configureDevelopmentSecurity(http);
        } else { // production profile
            configureProductionSecurity(http);
        }

        return http.build();
    }

    private void configureCommonSecurity(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/**").permitAll());
    }

    private void configureDevelopmentSecurity(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated());
    }

    private void configureProductionSecurity(HttpSecurity http) throws Exception {
        http.csrf(csrf ->
                        csrf
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                .ignoringRequestMatchers("/api/v1/auth/**"))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers.contentSecurityPolicy(csp -> csp.policyDirectives("""
                    default-src 'self';
                    frame-ancestors 'none';
                    script-src 'self';
                    style-src 'self';
                    img-src 'self' data:;
                    connect-src 'self';
                    font-src 'self';
                    base-uri 'none';
                    form-action 'self'
                """)).contentTypeOptions(Customizer.withDefaults()));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        boolean isDevelopment = DEV_PROFILE.equals(Arrays.stream(environment.getActiveProfiles()).findFirst().orElse(DEV_PROFILE));

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        if (isDevelopment) {
            configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        } else {
            configuration.setAllowedOrigins(List.of("https://production-domain-to-be-defined.com"));
        }

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

