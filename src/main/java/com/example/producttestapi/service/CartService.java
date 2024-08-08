package com.example.producttestapi.service;

import com.example.producttestapi.dto.CartDto;

public interface CartService {
     void deleteCart(int id) ;

    CartDto getCart();
}
