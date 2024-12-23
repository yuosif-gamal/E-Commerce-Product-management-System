package com.example.Ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic();
        http.authorizeHttpRequests(auth -> {
            auth
                    .requestMatchers(HttpMethod.GET, "/**").hasAnyAuthority("ADMIN", "USER", "MANAGER")
                    .requestMatchers(HttpMethod.POST, "/register").permitAll()
                    .requestMatchers(HttpMethod.POST, "/**").hasAnyAuthority("ADMIN", "MANAGER")
                    .requestMatchers(HttpMethod.DELETE, "/**").hasAnyAuthority("MANAGER")
                    .requestMatchers(HttpMethod.PUT, "/user").hasAnyAuthority("MANAGER" ,"ADMIN")
                    .anyRequest().authenticated();
        });

        return http.build();
    }
}
