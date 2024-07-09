package com.example.producttestapi.service;

import com.example.producttestapi.entities.Category;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();
    Category getCategory(int id);
    Category createCategory(Category category);

    void deleteCategory(int id);
}
