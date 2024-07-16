package com.example.producttestapi.service;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.mapper.CategoryMapper;
import com.example.producttestapi.repos.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<CategoryDto> getAllMainCategories() {
        List<Category> categories = categoryRepo.getAllMainCategories();
        List<CategoryDto> categoryDto = new ArrayList<>();
        for (Category category1 : categories ){
            CategoryDto c = CategoryMapper.convertEntityToDto(category1);
            categoryDto.add(c);
        }
        return categoryDto;
    }

    @Override
    public List<CategoryDto> getCategoryChildren(int categoryId) {
        Category category = categoryRepo.findById(Math.toIntExact(categoryId)).orElse(null);
        if (category ==null){
            throw new ResourceNotFoundException("no category with this ID : " + categoryId);
        }
        else {
            List<Category> categories = categoryRepo.getCategoryChildren(categoryId);
            List<CategoryDto> categoryDto = new ArrayList<>();
            for (Category category1 : categories ){
                CategoryDto c = CategoryMapper.convertEntityToDto(category1);
                categoryDto.add(c);
            }
            return categoryDto;
        }
    }

}