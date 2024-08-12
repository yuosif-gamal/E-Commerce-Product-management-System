package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.CartDto;

public interface CartService {
    void deleteCart(Long id);

    CartDto getCart();
}
