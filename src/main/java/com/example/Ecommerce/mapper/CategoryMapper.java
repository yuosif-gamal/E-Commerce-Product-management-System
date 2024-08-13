package com.example.Ecommerce.mapper;

import com.example.Ecommerce.dto.CategoryDto;
import com.example.Ecommerce.entity.Category;
import com.example.Ecommerce.model.CategoryModel;
import com.example.Ecommerce.dto.CategoryModelDto;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {
    public static CategoryDto convertEntityToDto(Category category) {
        return CategoryDto.builder()
                .name(category.getName())
                .build();
    }

    public static CategoryModelDto convertToModelDTO(CategoryModel categoryModel) {
        List<CategoryModelDto> subCategoriesDTO = new ArrayList<>();
        for (CategoryModel subCategory : categoryModel.getCategoriesModelList()) {
            subCategoriesDTO.add(convertToModelDTO(subCategory));
        }
        return CategoryModelDto.builder()
                .name(categoryModel.getName())
                .subCategories(subCategoriesDTO)
                .build();
    }
}
