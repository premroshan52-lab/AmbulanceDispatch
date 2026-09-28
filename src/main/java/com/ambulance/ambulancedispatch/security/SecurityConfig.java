package com.ambulance.ambulancedispatch.security;

import com.ambulance.ambulancedispatch.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(
            CustomUserDetailsService customUserDetailsService) {

        this.customUserDetailsService =
                customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                customUserDetailsService
        );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // Public login files
                .requestMatchers(
                        "/",
                        "/index.html",
                        "/style.css",
                        "/script.js",
                        "/login.html",
                        "/login.css",
                        "/login.js",
                        "/favicon.ico"
                ).permitAll()

                // Login endpoint
                .requestMatchers(
                        "/auth/login"
                ).permitAll()

                // -------------------------
                // ZONES
                // -------------------------

                .requestMatchers(
                        HttpMethod.GET,
                        "/zones/**"
                ).hasAnyRole(
                        "ADMIN",
                        "DISPATCHER",
                        "USER"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/zones/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/zones/**"
                ).hasRole("ADMIN")

                // -------------------------
                // ZONE DISTANCES
                // -------------------------

                .requestMatchers(
                        HttpMethod.GET,
                        "/zone-distances/**"
                ).authenticated()

                .requestMatchers(
                        HttpMethod.POST,
                        "/zone-distances/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/zone-distances/**"
                ).hasRole("ADMIN")

                // -------------------------
                // EMERGENCY CALLS
                // -------------------------

                .requestMatchers(
                        HttpMethod.GET,
                        "/emergency-calls/**"
                ).authenticated()

                .requestMatchers(
                        HttpMethod.POST,
                        "/emergency-calls"
                ).hasAnyRole(
                        "ADMIN",
                        "DISPATCHER"
                )

                .requestMatchers(
                        HttpMethod.PUT,
                        "/emergency-calls/*/arrive",
                        "/emergency-calls/*/complete"
                ).hasAnyRole(
                        "ADMIN",
                        "DISPATCHER"
                )

                // -------------------------
                // AMBULANCES
                // -------------------------

                .requestMatchers(
                        HttpMethod.GET,
                        "/ambulances/**"
                ).hasAnyRole(
                        "ADMIN",
                        "DISPATCHER",
                        "USER"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/ambulances"
                ).hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/ambulances/*/status"
                ).hasAnyRole(
                        "ADMIN",
                        "DISPATCHER"
                )

                // -------------------------
                // ANALYTICS
                // -------------------------

                .requestMatchers(
                        "/analytics/**"
                ).authenticated()

                // Everything else requires login
                .anyRequest()
                .authenticated()
            );

        return http.build();
    }
}