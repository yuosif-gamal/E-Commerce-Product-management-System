package com.example.producttestapi;

import com.example.producttestapi.entity.Role;
import com.example.producttestapi.entity.User;
import com.example.producttestapi.repository.RoleRepo;
import com.example.producttestapi.repository.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableCaching
public class ProductTestApiApplication {
    @Value("${user.default.email}")
    private String defaultEmail;

    @Value("${user.default.password}")
    private String defaultPassword;

    @Value("${user.default.firstName}")
    private String defaultFirstName;

    @Value("${user.default.lastName}")
    private String defaultLastName;

    public static void main(String[] args) {
        SpringApplication.run(ProductTestApiApplication.class, args);
    }
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return false;
            }
        };
    }
    @Bean
    public CommandLineRunner commandLineRunner(RoleRepo roleRepo, UserRepo userRepo , PasswordEncoder passwordEncoder){
        return args -> {
            if(roleRepo.findByName("USER") == null){
                Role role = new Role("USER");
                roleRepo.save(role);
            }
            if (userRepo.findAllByEmail(defaultEmail) == null) {
                String encodedPassword = passwordEncoder.encode(defaultPassword);
                User user = new User(defaultFirstName, defaultLastName, encodedPassword, defaultEmail);
                Role role = new Role("MANAGER");
                roleRepo.save(role);
                user.addRole(role);
                userRepo.save(user);
            }
        };
    }

}