package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.CartItemDto;
import com.example.Ecommerce.entity.CartItem;

public interface CartItemService {
    CartItem addCartItem(CartItem cartItem);

    CartItemDto decreaseOneFromItem(Long itemID);

    CartItemDto increaseOneFromItem(Long itemID);

    CartItemDto deleteItem(Long id);
}
