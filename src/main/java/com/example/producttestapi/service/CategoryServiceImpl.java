package com.example.producttestapi.service;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.mapper.CategoryMapper;
import com.example.producttestapi.model.CategoryModel;
import com.example.producttestapi.dto.CategoryModelDto;
import com.example.producttestapi.repos.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    @Autowired
    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Cacheable(value = "categories", key = "'all'")
    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepo.findAll();
        List<CategoryDto> categoryDto = convertToDto(categories);
        return categoryDto;
    }

    @Override
    @Cacheable(value = "categories",key = "#id")
    public Category getCategory(int id) {
        Category category = categoryRepo.findById(id).orElse(null);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        return category;
    }
    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(int id) {
        if (!categoryRepo.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepo.deleteById(id);
    }

    @Override
    @Cacheable(value = "categories" ,key = "'main")
    public List<CategoryDto> getAllMainCategories() {
        List<Category> categories = categoryRepo.getAllMainCategories();
        List<CategoryDto> categoryDto = convertToDto(categories);
        return categoryDto;
    }

    @Override
    @Cacheable(value = "categories", key = "'sub'")
    public List<CategoryDto> getCategoryChildren(int categoryId) {
        Category category = categoryRepo.findById(categoryId).orElse(null);
        if (category ==null){
            throw new ResourceNotFoundException("no category with this ID : " + categoryId);
        }
        else {
            List<Category> categories = categoryRepo.getCategoryChildren(categoryId);
            List<CategoryDto> categoryDto = convertToDto(categories);
            return categoryDto;
        }
    }

    private List<CategoryDto> convertToDto(List<Category> categories){
        List<CategoryDto> categoryDtos = categories.stream()
                .map(CategoryMapper::convertEntityToDto)
                .collect(Collectors.toList());
        return categoryDtos;
    }
    @Override
    @Cacheable(value = "categories", key = "'tree'")
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
                CategoryModelDto c = CategoryMapper.convertToModelDTO(categoryModel);
            finalResult.add(c);
        }

        return finalResult;
    }

}