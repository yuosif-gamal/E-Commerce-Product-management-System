package com.example.producttestapi;

import com.example.producttestapi.entities.Role;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.repos.RoleRepo;
import com.example.producttestapi.repos.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication

public class ProductTestApiApplication {

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
            if (userRepo.findAllByEmail("yousif@gmail.com") == null){
                String pass = passwordEncoder.encode("1234567");
                User user = new User("yousif" , "gamal",pass,"yousif@gmail.com");
                Role role = new Role("MANAGER");
                roleRepo.save(role);
                user.addRole(role);
                userRepo.save(user);
            }
        };
    }

}
