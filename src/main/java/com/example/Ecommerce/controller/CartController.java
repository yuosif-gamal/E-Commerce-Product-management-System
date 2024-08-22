package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.CartDto;
import com.example.Ecommerce.dto.SuccessResponse;
import com.example.Ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final static Logger LOGGER = LoggerFactory.getLogger(CartController.class);

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
        LOGGER.info("Received request to retrieve cart");
        CartDto cart = cartService.getCart();
        LOGGER.info("Returning cart with {} items", cart.getCartItemDTOs().size());
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
        LOGGER.info("Received request to delete cart item with ID {}", id);
        cartService.deleteCart(id);
        LOGGER.info("Cart with ID {} deleted successfully", id);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Cart deleted successfully", null, HttpStatus.OK.value()));
    }
}
