package com.example.Ecommerce.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Setter
@Getter
public class VoucherDto {
    @NotBlank(message = "Code is required")
    private String code;
    @NotNull(message = "discount is required")
    @Column(unique = true)
    @Min(value = 0, message = "Discount must be a non-negative number")
    private BigDecimal discount;
    @NotNull(message = "expire date is required")

    private Date expiryDate;
}
