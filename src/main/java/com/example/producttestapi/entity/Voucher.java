package com.example.producttestapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "discount is required")
    private String code;
    @NotNull(message = "discount is required")
    @Min(value = 1, message = "Discount must be a non-negative number")
    private BigDecimal discount;

    @NotNull(message = "expire date is required")

    private Date expireDate;
    @OneToMany(mappedBy = "voucherCode")
    @JsonIgnore
    private List<Product> products;

}
