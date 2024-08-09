package com.example.Ecommerce.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.*;
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
    private Long id ;
    @NotBlank(message = "Product name is required")
    private String name;
    @NotBlank(message = "description is required")

    private String description;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0" ,message = "Price should be non-negative ")
    private Double price;
    @NotNull(message = "category id is required")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category ;

    @ManyToOne(fetch = FetchType.EAGER)

    @JoinColumn(name = "voucher_code")
    private Voucher voucherCode;
    @Min(value = 0, message = "Quantity must be a non-negative number")
    private Integer quantity;

}
