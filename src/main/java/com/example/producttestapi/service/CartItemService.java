package com.example.producttestapi.service;

import com.example.producttestapi.dto.CartItemDto;
import com.example.producttestapi.entity.CartItem;

public interface CartItemService {
    CartItem addCartItem(CartItem cartItem);

    CartItemDto decreaseOneFromItem(int itemID);

    CartItemDto increaseOneFromItem(int itemID);

    CartItemDto deleteItem(int id);
}
