package com.example.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class CategoryModelDto {
    private String name;
    private List<CategoryModelDto> subCategories;
}
