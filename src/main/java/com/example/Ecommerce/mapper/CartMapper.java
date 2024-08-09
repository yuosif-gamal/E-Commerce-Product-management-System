package com.example.Ecommerce.mapper;

import com.example.Ecommerce.dto.CartDto;
import com.example.Ecommerce.dto.CartItemDto;
import com.example.Ecommerce.entity.Cart;
import com.example.Ecommerce.entity.CartItem;

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
