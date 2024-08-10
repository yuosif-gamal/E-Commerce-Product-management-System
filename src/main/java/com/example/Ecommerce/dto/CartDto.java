package com.example.Ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
public class CartDto {
    private Double totalPrice;
    private List<CartItemDto> cartItemDTOs;
}
