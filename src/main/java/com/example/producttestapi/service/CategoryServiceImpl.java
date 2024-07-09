package com.example.producttestapi.service;

import com.example.producttestapi.entities.Category;
import com.example.producttestapi.repos.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    @Autowired
    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }

    @Override
    public Category getCategory(int id) {
        Optional<Category> optionalCategory = categoryRepo.findById(id);
        return optionalCategory.orElse(null);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepo.deleteById(id);
    }
}