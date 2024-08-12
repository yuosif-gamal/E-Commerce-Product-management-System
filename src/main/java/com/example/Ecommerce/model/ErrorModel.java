package com.example.Ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorModel {
    private String message;
    private LocalDateTime localDateTime;
}
