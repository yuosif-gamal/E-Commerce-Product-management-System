package com.example.Ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private int quantityToTake;
    private Double pricePerItem;
}
