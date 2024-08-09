package com.example.Ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @NotBlank(message = "Product name is required")
    private String name;
    @NotBlank(message = "description name is required")

    private String description;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0" ,message = "should be non-negative ")
    private double price;
    @NotNull(message = "category id is required")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category ;

    @ManyToOne(fetch = FetchType.EAGER)

    @JoinColumn(name = "voucher_code")
    private Voucher voucherCode;
    @Min(value = 0, message = "Quantity must be a non-negative number")
    private int quantity;

}
