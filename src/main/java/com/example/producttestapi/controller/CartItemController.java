package com.example.producttestapi.controller;

import com.example.producttestapi.dto.SuccessResponse;
import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("")
    public ResponseEntity<SuccessResponse> addCartItemToCart(@RequestBody CartItem  cartItem) {
        CartItem createItem = cartItemService.addCartItem(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Item created successfully", true, createItem, HttpStatus.CREATED.value()));
    }
}
