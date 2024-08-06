package com.example.producttestapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CartDto {
    private double totalPrice;
    private List<CartItemDto> cartItemDtos;
}
