package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.CartDto;
import com.example.Ecommerce.dto.SuccessResponse;
import com.example.Ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(
            summary = "Get Cart",
            description = "Retrieves the current cart information."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<SuccessResponse> getCart() {
        CartDto cart = cartService.getCart();
        return ResponseEntity.ok(new SuccessResponse("Cart retrieved successfully", cart, HttpStatus.OK.value()));
    }

    @Operation(
            summary = "Delete Cart Item",
            description = "Deletes an item from the cart based on the provided item ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteCart(@PathVariable("id") Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Cart item deleted successfully", null, HttpStatus.OK.value()));
    }
}
