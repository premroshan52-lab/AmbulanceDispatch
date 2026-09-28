package com.ambulance.ambulancedispatch.controller;

import com.ambulance.ambulancedispatch.dto.LoginRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager
            authenticationManager;

    private final SecurityContextRepository
            securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public AuthController(
            AuthenticationManager
                    authenticationManager) {

        this.authenticationManager =
                authenticationManager;
    }

    // =====================================================
    // LOGIN
    // =====================================================

    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(
                authentication
        );

        SecurityContextHolder.setContext(
                context
        );

        securityContextRepository.saveContext(
                context,
                httpRequest,
                httpResponse
        );

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "message",
                "Login successful"
        );

        response.put(
                "username",
                authentication
                        .getName()
        );

        response.put(
                "role",
                authentication
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
                        .replace(
                                "ROLE_",
                                ""
                        )
        );

        return response;
    }

    // =====================================================
    // CURRENT USER
    // =====================================================

    @GetMapping("/me")
    public Map<String, Object> currentUser(
            Authentication authentication) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "username",
                authentication.getName()
        );

        response.put(
                "role",
                authentication
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
                        .replace(
                                "ROLE_",
                                ""
                        )
        );

        return response;
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    @PostMapping("/logout")
    public Map<String, String> logout(
            HttpServletRequest request) {

        request.getSession().invalidate();

        SecurityContextHolder.clearContext();

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "message",
                "Logout successful"
        );

        return response;
    }
}