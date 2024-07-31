package com.example.producttestapi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    private String name;
    private String description;
    private double price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryID ;

    @ManyToOne(fetch = FetchType.EAGER)

    @JoinColumn(name = "voucher_code")
    private Voucher voucherCode;

    private int quantity;

}
