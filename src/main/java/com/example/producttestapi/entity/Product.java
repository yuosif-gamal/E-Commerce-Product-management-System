package com.example.producttestapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

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
    @NotBlank(message = "Product name is required")
    private String name;
    @NotBlank(message = "description name is required")

    private String description;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "1" ,message = "should be non-negative or zero")

    private double price;
    @NotNull(message = "category id is required")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryID ;

    @ManyToOne(fetch = FetchType.EAGER)

    @JoinColumn(name = "voucher_code")
    private Voucher voucherCode;
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be a non-negative number")
    private int quantity;
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<CartItem> cartItems;

}
