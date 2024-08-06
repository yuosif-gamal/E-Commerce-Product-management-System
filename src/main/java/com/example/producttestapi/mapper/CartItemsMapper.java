package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.CartDto;
import com.example.producttestapi.dto.CartItemDto;
import com.example.producttestapi.entities.CartItem;

import java.util.List;

public class CartItemsMapper {
    public  static CartItemDto convertToDTO(CartItem item){
        CartItemDto cartItemDto =new CartItemDto();
        cartItemDto.setPricePerItem(item.getPricePerItem());
        cartItemDto.setQuantityToTake(item.getQuantityToTake());
        return cartItemDto;
    }
}

