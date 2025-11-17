package io.github.mouhamethfadal.blogbackend.config;


import io.github.mouhamethfadal.blogbackend.security.JwtAuthenticationFilter;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Generated
public class SecurityConfig {
    protected static final String[] ALLOWED_ENDPOINTS = {"/actuator/**", "/swagger-ui/**", "/swagger-ui.html", "/api-docs/**"};
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final Environment environment;
    private static final String DEV_PROFILE = "dev";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String activeProfile = Arrays.stream(environment.getActiveProfiles()).findFirst().orElse(DEV_PROFILE);

        configureCommonSecurity(http);

        if (DEV_PROFILE.equals(activeProfile)) {
            configureDevelopmentSecurity(http);
        } else { // production profile
            configureProductionSecurity(http);
        }

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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
                // CSRF protection is disabled because this is a stateless API using JWT tokens for authentication,
                // not cookies. CSRF protection isn't necessary in this context since we're not using session cookies.
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.
                        requestMatchers(ALLOWED_ENDPOINTS).permitAll()
                        .anyRequest().authenticated());
    }

    private void configureProductionSecurity(HttpSecurity http) throws Exception {
        http
                // CSRF protection is disabled because this is a stateless API using JWT tokens for authentication,
                // not cookies. CSRF protection isn't necessary in this context since we're not using session cookies.
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ALLOWED_ENDPOINTS).permitAll()
                        .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        boolean isDevelopment = DEV_PROFILE.equals(Arrays.stream(environment.getActiveProfiles()).findFirst().orElse(DEV_PROFILE));

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of());

        if (isDevelopment) {
            configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080", "http://blog.localhost"));
        } else {
            configuration.setAllowedOrigins(List.of("https://production-domain-to-be-defined.com"));
        }

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

