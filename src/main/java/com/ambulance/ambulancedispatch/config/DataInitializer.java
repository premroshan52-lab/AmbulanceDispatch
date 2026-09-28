package com.ambulance.ambulancedispatch.config;

import com.ambulance.ambulancedispatch.entity.AppUser;
import com.ambulance.ambulancedispatch.repository.AppUserRepository;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner
    initializeUsers(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // =================================================
            // ADMIN
            // =================================================

            if (!appUserRepository
                    .existsByUsername("admin")) {

                AppUser admin =
                        new AppUser();

                admin.setUsername("admin");

                admin.setPassword(
                        passwordEncoder.encode(
                                "admin123"
                        )
                );

                admin.setRole("ADMIN");

                appUserRepository.save(admin);
            }

            // =================================================
            // DISPATCHER
            // =================================================

            if (!appUserRepository
                    .existsByUsername("dispatcher")) {

                AppUser dispatcher =
                        new AppUser();

                dispatcher.setUsername(
                        "dispatcher"
                );

                dispatcher.setPassword(
                        passwordEncoder.encode(
                                "dispatch123"
                        )
                );

                dispatcher.setRole(
                        "DISPATCHER"
                );

                appUserRepository.save(
                        dispatcher
                );
            }

            // =================================================
            // USER
            // =================================================

            if (!appUserRepository
                    .existsByUsername("user")) {

                AppUser user =
                        new AppUser();

                user.setUsername("user");

                user.setPassword(
                        passwordEncoder.encode(
                                "user123"
                        )
                );

                user.setRole("USER");

                appUserRepository.save(user);
            }
        };
    }
}