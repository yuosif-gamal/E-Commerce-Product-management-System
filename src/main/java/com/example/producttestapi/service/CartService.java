package com.example.producttestapi.service;

import com.example.producttestapi.dto.CartDto;
import com.example.producttestapi.entities.Cart;

import java.util.List;

public interface CartService {
     void deleteCart(int id) ;

    CartDto getCart();
}
