package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.CartItemDto;
import com.example.Ecommerce.dto.SuccessResponse;
import com.example.Ecommerce.entity.CartItem;
import com.example.Ecommerce.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;
    private final static Logger LOGGER = LoggerFactory.getLogger(CartItemController.class);

    @PostMapping
    @Operation(summary = "Add Item to Cart", description = "Add a new item to the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid item details provided")
    })
    public ResponseEntity<SuccessResponse> addCartItemToCart(@Valid @RequestBody CartItem cartItem) {
        LOGGER.info("Received request to add item to cart: {}", cartItem);
        CartItem createdItem = cartItemService.addCartItem(cartItem);
        LOGGER.info("Item added to cart successfully: {}", createdItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Item created successfully", createdItem, HttpStatus.CREATED.value()));
    }

    @PutMapping("/add-one/{id}")
    @Operation(summary = "Increase Item Quantity", description = "Increase the quantity of a cart item by one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item quantity increased successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<SuccessResponse> increaseOneFromItem(@PathVariable("id") Long id) {
        LOGGER.info("Received request to increase quantity of item with ID {}", id);
        CartItemDto item = cartItemService.increaseOneFromItem(id);
        LOGGER.info("Increased quantity of item with ID {}: {}", id, item);
        return ResponseEntity.ok(new SuccessResponse("Item increased successfully", item, HttpStatus.OK.value()));
    }

    @PutMapping("/remove-one/{id}")
    @Operation(summary = "Decrease Item Quantity", description = "Decrease the quantity of a cart item by one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item quantity decreased successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<SuccessResponse> decreaseOneFromItem(@PathVariable("id") Long id) {
        LOGGER.info("Received request to decrease quantity of item with ID {}", id);
        CartItemDto item = cartItemService.decreaseOneFromItem(id);
        LOGGER.info("Decreased quantity of item with ID {}: {}", id, item);
        return ResponseEntity.ok(new SuccessResponse("Item decreased successfully", item, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Item from Cart", description = "Delete an item from the cart by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<SuccessResponse> deleteItem(@PathVariable("id") Long id) {
        LOGGER.info("Received request to delete item with ID {}", id);
        CartItemDto item = cartItemService.deleteItem(id);
        LOGGER.info("Item with ID {} deleted successfully: {}", id, item);
        return ResponseEntity.ok(new SuccessResponse("Item deleted successfully", item, HttpStatus.OK.value()));
    }
}
