package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.CartItemDto;
import com.example.producttestapi.entity.CartItem;

public class CartItemsMapper {
    public  static CartItemDto convertToDTO(CartItem item){
        CartItemDto cartItemDto =new CartItemDto();
        cartItemDto.setPricePerItem(item.getPricePerItem());
        cartItemDto.setQuantityToTake(item.getQuantityToTake());
        return cartItemDto;
    }
}

