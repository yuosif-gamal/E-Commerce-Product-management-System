package com.example.producttestapi.controller;

import com.example.producttestapi.dto.CartItemDto;
import com.example.producttestapi.dto.SuccessResponse;
import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> addCartItemToCart(@RequestBody CartItem  cartItem) {
        CartItem createItem = cartItemService.addCartItem(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Item created successfully", true, createItem, HttpStatus.CREATED.value()));
    }
    @PutMapping("/add-one/{id}")
    public ResponseEntity<SuccessResponse> increaseOneFromItem(@PathVariable("id") int id) {
        CartItemDto item = cartItemService.increaseOneFromItem(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Item increased successfully", true, item, HttpStatus.CREATED.value()));
    }
    @PutMapping("/remove-one/{id}")
    public ResponseEntity<SuccessResponse> decreaseOneFromItem(@PathVariable("id") int  id) {
        CartItemDto item = cartItemService.decreaseOneFromItem(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Item decreased successfully", true, item, HttpStatus.CREATED.value()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteItem(@PathVariable("id") int  id) {
        CartItemDto item = cartItemService.deleteItem(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Item deleted successfully", true, item, HttpStatus.CREATED.value()));
    }
}
