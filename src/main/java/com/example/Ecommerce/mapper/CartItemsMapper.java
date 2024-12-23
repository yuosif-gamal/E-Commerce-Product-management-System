package com.example.Ecommerce.mapper;

import com.example.Ecommerce.dto.CartItemDto;
import com.example.Ecommerce.entity.CartItem;

import java.util.stream.Collectors;

public class CartItemsMapper {

    public static CartItemDto convertToDTO(CartItem item) {
        return CartItemDto.builder()
                .pricePerItem(item.getPricePerItem())
                .quantityToTake(item.getQuantityToTake())
                .status(item.getStatus())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .product(ProductMapper.convertEntityToDto(item.getProduct()))
                .build();
    }
}
