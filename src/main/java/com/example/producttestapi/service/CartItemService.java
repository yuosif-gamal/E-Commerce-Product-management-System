package com.example.producttestapi.service;

import com.example.producttestapi.dto.CartItemDto;
import com.example.producttestapi.entity.CartItem;

public interface CartItemService {
    CartItem addCartItem(CartItem cartItem);

    CartItemDto decreaseOneFromItem(Long itemID);

    CartItemDto increaseOneFromItem(Long itemID);

    CartItemDto deleteItem(Long id);
}
