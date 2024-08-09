package com.example.Ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CartDto {
    private double totalPrice;
    private List<CartItemDto> cartItemDtos;
}
