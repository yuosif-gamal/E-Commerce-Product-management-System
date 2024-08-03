package com.example.producttestapi.service;

import com.example.producttestapi.entities.CartItem;

public interface CartItemService {
    CartItem addCartItem(CartItem cartItem);

    CartItem decreaseOneFromItem(int itemID);

    CartItem increaseOneFromItem(int itemID);

    CartItem deleteItem(int id);
}
