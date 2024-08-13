package com.example.Ecommerce.mapper;

import com.example.Ecommerce.dto.CartItemDto;
import com.example.Ecommerce.entity.CartItem;

public class CartItemsMapper {

    public static CartItemDto convertToDTO(CartItem item) {
        return CartItemDto.builder()
                .pricePerItem(item.getPricePerItem())
                .quantityToTake(item.getQuantityToTake())
                .build();
    }
}
