package com.example.producttestapi.service;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.dto.CategoryModelDto;

import java.util.List;

public interface CategoryService {
    Category getCategory(int parent_id);
    Category createCategory(Category category);
    void deleteCategory(int id);
    List<CategoryDto> getAllMainCategories();

    List<CategoryDto> getCategoryChildren(int categoryId);


    List<CategoryModelDto> getCategoriesTree();
}
