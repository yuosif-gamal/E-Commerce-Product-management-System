package com.example.producttestapi.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter

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



}
