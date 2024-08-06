package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.CartDto;
import com.example.producttestapi.dto.CartItemDto;
import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.entities.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
