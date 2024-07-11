package com.example.producttestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http ) throws  Exception{
        http.csrf().disable();
        http.httpBasic();
        http.authorizeHttpRequests(
          auth -> {
              auth.requestMatchers(HttpMethod.GET, "/api/**").hasAnyAuthority("ADMIN","USER","MANAGER")
                      .requestMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority("ADMIN","MANAGER")
                      .requestMatchers(HttpMethod.DELETE,"/api/**").hasAnyAuthority("MANAGER");
              auth.anyRequest().permitAll();
          }
        );
        return http.build();

    }
}
