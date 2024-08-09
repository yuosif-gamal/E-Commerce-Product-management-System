package com.example.Ecommerce.mapper;

import com.example.Ecommerce.dto.CartItemDto;
import com.example.Ecommerce.entity.CartItem;

public class CartItemsMapper {
    public  static CartItemDto convertToDTO(CartItem item){
        CartItemDto cartItemDto =new CartItemDto();
        cartItemDto.setPricePerItem(item.getPricePerItem());
        cartItemDto.setQuantityToTake(item.getQuantityToTake());
        return cartItemDto;
    }
}

