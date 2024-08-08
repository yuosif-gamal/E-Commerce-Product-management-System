package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entity.Category;
import com.example.producttestapi.model.CategoryModel;
import com.example.producttestapi.dto.CategoryModelDto;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {
    public static CategoryDto convertEntityToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static CategoryModelDto convertToModelDTO(CategoryModel categoryModel) {
        List<CategoryModelDto> subCategoriesDTO = new ArrayList<>();
        for (CategoryModel subCategory : categoryModel.getCategoriesModelList()) {
            subCategoriesDTO.add(convertToModelDTO(subCategory));
        }
        return new CategoryModelDto(categoryModel.getName(), subCategoriesDTO);
    }

}
