package com.example.producttestapi.service;

import com.example.producttestapi.entities.Category;
import com.example.producttestapi.exception.ResourceNotFoundException;
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
    public Category getCategory(int id) {
        Category category = categoryRepo.findById(id).orElse(null);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        return category;
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public void deleteCategory(int id) {
        if (!categoryRepo.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepo.deleteById(id);
    }

    @Override
    public List<Category> getAllMainCategories() {
        return categoryRepo.getAllMainCategories();
    }

    @Override
    public List<Category> getCategoryChildren(int categoryId) {
        Category category = categoryRepo.findById(Math.toIntExact(categoryId)).orElse(null);

        if (category ==null){
            throw new ResourceNotFoundException("no category with this ID : " + categoryId);
        }
        else
            return categoryRepo.getCategoryChildren(categoryId);
    }

}