package com.example.springredditclone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // disabling csrf because csrf attacks can occur when there are sessions and cookies in use to authenticate session information
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                                    auth.requestMatchers("/api/auth/**")    // allow all incoming requests that start with the endpoint "/api/auth/...", and any other requests that don't match it has to be authenticated
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated()
                );

        return http.build();
    }

    // PasswordEncoder: Spring Security's interface that is used to perform a one-way transformation of a password to let the password be stored securely
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
