package com.example.Ecommerce.dto;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CartItemDto {
    private Integer quantityToTake;
    private Double pricePerItem;
}
