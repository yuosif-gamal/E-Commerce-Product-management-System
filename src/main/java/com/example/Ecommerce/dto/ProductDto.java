package com.example.Ecommerce.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private String name;

    private String description;

    private Double price;

    private Integer quantity;
}
