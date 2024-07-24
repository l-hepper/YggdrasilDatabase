package com.mjolnir.yggdrasil.config;

import com.mjolnir.yggdrasil.service.MjolnirApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final MjolnirApiService mjolnirApiService;

    public SecurityConfig(MjolnirApiService mjolnirApiService) {
        this.mjolnirApiService = mjolnirApiService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest.requestMatchers("/", "/welcome").permitAll()
                                .requestMatchers("/countries/**").hasRole("ADMIN")
                                .requestMatchers("/cities/**").hasRole("ADMIN")
                                .requestMatchers("/languages/**").hasRole("ADMIN")
                                .requestMatchers("/countries").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/cities").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/languages").hasAnyRole("ADMIN", "USER")
                                .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin.loginPage("/login").permitAll())
                .logout(logout -> logout.logoutUrl("/logout").permitAll())
                .build();
    }
}
