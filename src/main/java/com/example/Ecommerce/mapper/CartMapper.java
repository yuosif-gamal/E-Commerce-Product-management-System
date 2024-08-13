package com.example.Ecommerce.mapper;

import com.example.Ecommerce.dto.CartDto;
import com.example.Ecommerce.dto.CartItemDto;
import com.example.Ecommerce.entity.Cart;
import com.example.Ecommerce.entity.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartDto convertEntityToDto(Cart cart) {
        return CartDto.builder()
                .totalPrice(cart.getTotalPrice())
                .cartItemDTOs(
                        cart.getItems().stream()
                                .map(CartItemsMapper::convertToDTO)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
