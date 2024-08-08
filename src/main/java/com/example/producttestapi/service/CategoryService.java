package com.example.producttestapi.service;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entity.Category;
import com.example.producttestapi.dto.CategoryModelDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategory();

    Category getCategory(int parent_id);
    Category createCategory(Category category);
    void deleteCategory(int id);
    List<CategoryDto> getAllMainCategories();

    List<CategoryDto> getCategoryChildren(int categoryId);


    List<CategoryModelDto> getCategoriesTree();
}
