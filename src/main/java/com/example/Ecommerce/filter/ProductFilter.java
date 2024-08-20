package com.example.Ecommerce.filter;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    private Double minPrice;
    private Double maxPrice;
    private String productNameContain;
    private int minQuantity;

    public ProductFilter(Double minPrice, Double maxPrice, int minQuantity) {
    }
}