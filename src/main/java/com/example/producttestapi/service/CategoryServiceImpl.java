package com.example.producttestapi.service;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.mapper.CategoryMapper;
import com.example.producttestapi.model.CategoryModel;
import com.example.producttestapi.dto.CategoryModelDto;
import com.example.producttestapi.repos.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        Category category = categoryRepo.findById(categoryId).orElse(null);
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

    @Override
    public List<CategoryModelDto> getCategoriesTree() {
        List<Category> categoriesList = categoryRepo.findAll();
        Map<Integer, CategoryModel> categoryModelMap = new HashMap<>();
        List<CategoryModel> result = new ArrayList<>();

        for (Category cat : categoriesList) {
            CategoryModel categoryModel = new CategoryModel(cat.getId(), cat.getName(), new ArrayList<>());
            categoryModelMap.put(cat.getId(), categoryModel);
        }

        for (Category cat : categoriesList) {
            CategoryModel categoryModel = categoryModelMap.get(cat.getId());

            if (cat.getParentCategory() != null) {
                CategoryModel parentCategoryModel = categoryModelMap.get(cat.getParentCategory().getId());
                parentCategoryModel.getCategoriesModelList().add(categoryModel);
            } else {
                result.add(categoryModel);
            }
        }
        List<CategoryModelDto> finalResult = new ArrayList<>();
        for (CategoryModel categoryModel : result) {
            CategoryModelDto c = CategoryMapper.convertToDTO(categoryModel);
            finalResult.add(c);
        }

        return finalResult;
    }

}