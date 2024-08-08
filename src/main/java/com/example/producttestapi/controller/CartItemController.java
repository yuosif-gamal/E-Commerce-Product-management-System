package com.example.producttestapi.controller;

import com.example.producttestapi.dto.CartItemDto;
import com.example.producttestapi.dto.SuccessResponse;
import com.example.producttestapi.entity.CartItem;
import com.example.producttestapi.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
    @Operation(summary = "Add Item to Cart", description = "Add a new item to the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid item details provided")
    })
    public ResponseEntity<SuccessResponse> addCartItemToCart(@Valid @RequestBody CartItem cartItem) {
        CartItem createItem = cartItemService.addCartItem(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Item created successfully", true, createItem, HttpStatus.CREATED.value()));
    }

    @PutMapping("/add-one/{id}")
    @Operation(summary = "Increase Item Quantity", description = "Increase the quantity of a cart item by one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item quantity increased successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<SuccessResponse> increaseOneFromItem(@PathVariable("id") int id) {
        CartItemDto item = cartItemService.increaseOneFromItem(id);
        return ResponseEntity.ok(new SuccessResponse("Item increased successfully", true, item, HttpStatus.OK.value()));
    }

    @PutMapping("/remove-one/{id}")
    @Operation(summary = "Decrease Item Quantity", description = "Decrease the quantity of a cart item by one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item quantity decreased successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<SuccessResponse> decreaseOneFromItem(@PathVariable("id") int id) {
        CartItemDto item = cartItemService.decreaseOneFromItem(id);
        return ResponseEntity.ok(new SuccessResponse("Item decreased successfully", true, item, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Item from Cart", description = "Delete an item from the cart by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<SuccessResponse> deleteItem(@PathVariable("id") int id) {
        CartItemDto item = cartItemService.deleteItem(id);
        return ResponseEntity.ok(new SuccessResponse("Item deleted successfully", true, item, HttpStatus.OK.value()));
    }
}
