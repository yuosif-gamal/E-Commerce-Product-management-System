package com.example.Ecommerce.dto;

import com.example.Ecommerce.entity.CartItemStatus;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CartItemDto {
    private Integer quantityToTake;
    private Double pricePerItem;
    private CartItemStatus status;
    private ProductDto product;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
