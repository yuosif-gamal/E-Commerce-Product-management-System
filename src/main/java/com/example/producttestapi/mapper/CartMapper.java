package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.CartDto;
import com.example.producttestapi.dto.CartItemDto;
import com.example.producttestapi.entity.Cart;
import com.example.producttestapi.entity.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartMapper {
    public static CartDto convertEntityToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setTotalPrice(cart.getTotalPrice());
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            CartItemDto cartItemDto = CartItemsMapper.convertToDTO(item);
            cartItemDtos.add(cartItemDto);
        }

        cartDto.setCartItemDtos(cartItemDtos);
        return cartDto;
    }
}
