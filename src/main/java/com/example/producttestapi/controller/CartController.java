package com.example.producttestapi.controller;

import com.example.producttestapi.dto.CartDto;
import com.example.producttestapi.dto.SuccessResponse;
import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping
    public ResponseEntity<SuccessResponse> getCart(){
        CartDto cart = cartService.getCart();
        return  ResponseEntity.ok(new SuccessResponse("cart retrieved successfully", true, cart, HttpStatus.OK.value()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteItem(@PathVariable("id") int  id) {
        cartService.deleteCart(id);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Cart deleted successfully", true, null, HttpStatus.OK.value()));
    }
}
