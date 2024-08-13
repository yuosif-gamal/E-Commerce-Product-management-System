package com.example.Ecommerce.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Builder
public class CategoryModelDto {
    private String name;
    private List<CategoryModelDto> subCategories;
}
