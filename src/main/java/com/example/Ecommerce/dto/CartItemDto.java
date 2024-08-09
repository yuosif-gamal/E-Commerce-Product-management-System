package com.example.Ecommerce.dto;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemDto {
    private Integer quantityToTake;
    private Double pricePerItem;
}
