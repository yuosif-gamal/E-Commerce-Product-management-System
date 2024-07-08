package com.example.producttestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

public class ProductTestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductTestApiApplication.class, args);
    }

}
