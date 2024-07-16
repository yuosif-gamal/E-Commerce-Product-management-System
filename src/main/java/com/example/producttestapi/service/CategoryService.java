package com.example.producttestapi.service;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Category;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category getCategory(int parent_id);
    Category createCategory(Category category);

    void deleteCategory(int id);
    List<CategoryDto> getAllMainCategories();

    List<CategoryDto> getCategoryChildren(int categoryId);
}
