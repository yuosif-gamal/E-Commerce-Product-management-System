package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.entities.User;

public class CategoryMapper {
    public static CategoryDto convertEntityToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
