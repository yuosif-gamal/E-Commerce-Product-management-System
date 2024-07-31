package com.example.producttestapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)

    private Product product;
    private int quantity_to_take;
    private int price_per_one;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore

    private Cart cart;
}
