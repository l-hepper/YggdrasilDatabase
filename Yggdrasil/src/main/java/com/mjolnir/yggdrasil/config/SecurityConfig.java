package com.mjolnir.yggdrasil.config;

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

        @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest ->
                        //All
                        authRequest.requestMatchers("/", "/welcome").permitAll()
                                .requestMatchers("/css/**", "/images/**", "/javascript/**").permitAll()

                                //Auth
                                .requestMatchers("/Yggdrasil").authenticated()

                                //Countries
                                .requestMatchers("/Yggdrasil/api/countries/edit/*", "/Yggdrasil/api/countries/update", "/Yggdrasil/api/countries/delete/**").hasRole("ADMIN") //api
                                .requestMatchers("/countries/edit/**", "/countries/create", "/countries/update/**", "/countries/delete/**").hasRole("ADMIN")
                                .requestMatchers("/countries", "/countries/search").authenticated()

                                //Cities
                                .requestMatchers("/Yggdrasil/api/cities/**", "/Yggdrasil/api/cities/**", "/Yggdrasil/api/cities/**").hasRole("ADMIN") //api
                                .requestMatchers("/cities/**").authenticated()

                                //Languages
                                .requestMatchers("/Yggdrasil/api/languages/**").hasRole("ADMIN") //api
                                .requestMatchers("/languages/**").authenticated())

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/Yggdrasil", true)
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout").permitAll())
                .build();
    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authRequest ->
//                        authRequest.anyRequest().permitAll())
//                .formLogin(formLogin -> formLogin.loginPage("/login")
//                        .defaultSuccessUrl("/Yggdrasil", true)
//                        .permitAll())
//                .logout(logout -> logout.logoutUrl("/logout").permitAll())
//                .build();
//    }
}
