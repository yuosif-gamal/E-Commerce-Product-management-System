package com.example.producttestapi.service;

import com.example.producttestapi.dto.CartDto;

public interface CartService {
     void deleteCart(Long id) ;

    CartDto getCart();
}
