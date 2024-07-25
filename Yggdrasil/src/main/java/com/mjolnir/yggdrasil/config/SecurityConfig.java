package com.mjolnir.yggdrasil.config;

import com.mjolnir.yggdrasil.service.MjolnirApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest ->
                        authRequest.requestMatchers("/", "/welcome").permitAll()
                                .requestMatchers("/Yggdrasil/countries/create", "/Yggdrasil/countries/**/edit", "/Yggdrasil/countries/**/delete").hasRole("ADMIN")
                                .requestMatchers("/Yggdrasil/cities/create", "/Yggdrasil/cities/**/edit", "/Yggdrasil/cities/**/delete").hasRole("ADMIN")
                                .requestMatchers("/Yggdrasil/languages/create", "/Yggdrasil/languages/**/edit", "/Yggdrasil/languages/**/delete").hasRole("ADMIN")
                                .requestMatchers("/Yggdrasil/countries/**").authenticated()
                                .requestMatchers("/Yggdrasil/cities/**").authenticated()
                                .requestMatchers("/Yggdrasil/languages/**").authenticated()
                                .requestMatchers("/Yggdrasil").authenticated())
                .formLogin(formLogin -> formLogin.loginPage("/login").permitAll())
                .logout(logout -> logout.logoutUrl("/logout").permitAll())
                .build();
    }
}
