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
                                .requestMatchers("/css/**", "/images/**").permitAll()
                                .requestMatchers("/Yggdrasil/countries/edit/*", "/Yggdrasil/countries/update", "/Yggdrasil/countries/delete/**").hasRole("ADMIN")
                                .requestMatchers("/Yggdrasil/cities/**", "/Yggdrasil/cities/**", "/Yggdrasil/cities/**").hasRole("ADMIN")
                                .requestMatchers("/Yggdrasil/languages/**").hasRole("ADMIN")
                                .requestMatchers("/countries/**").authenticated()
                                .requestMatchers("/cities/**").authenticated()
                                .requestMatchers("/languages/**").authenticated()
                                .requestMatchers("/Yggdrasil").authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/Yggdrasil", true)
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout").permitAll())
                .build();
    }




}
