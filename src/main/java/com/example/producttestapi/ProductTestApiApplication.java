package com.example.producttestapi;

import com.example.producttestapi.entities.Role;
import com.example.producttestapi.repos.RoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class ProductTestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductTestApiApplication.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner(RoleRepo roleRepo){
        return args -> {
            if(roleRepo.findByName("USER") == null){
                Role role = new Role("USER");
                roleRepo.save(role);
            }
        };
    }

}
