package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.CategoryDto;
import com.example.Ecommerce.entity.Category;
import com.example.Ecommerce.dto.CategoryModelDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategory();

    CategoryDto getCategory(Long parent_id);
    Category createCategory(Category category);
    void deleteCategory(Long id);
    List<CategoryDto> getAllMainCategories();

    List<CategoryDto> getCategoryChildren(Long categoryId);


    List<CategoryModelDto> getCategoriesTree();
}
