package com.SpringBoot_essentials.curso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desativa proteção contra CSRF (recomendado só em dev)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permite qualquer requisição, sem login
                )
                .build();
    }
}
