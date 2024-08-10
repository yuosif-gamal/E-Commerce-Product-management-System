package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.CategoryDto;
import com.example.Ecommerce.entity.Category;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.CategoryMapper;
import com.example.Ecommerce.model.CategoryModel;
import com.example.Ecommerce.dto.CategoryModelDto;
import com.example.Ecommerce.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    @Cacheable(value = "categories", key = "'all'")
    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepo.findAll();
        List<CategoryDto> categoryDto = convertToDto(categories);
        return categoryDto;
    }

    @Override
    @Cacheable(value = "categories",key = "#id")
    public CategoryDto getCategory(Long id) {
        Category category = categoryRepo.findById(id).orElse(null);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        CategoryDto categoryDto = CategoryMapper.convertEntityToDto(category);
        return categoryDto;
    }
    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(Long id) {
        if (!categoryRepo.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepo.deleteById(id);
    }

    @Override
    @Cacheable(value = "categories", key = "'main'")
    public List<CategoryDto> getAllMainCategories() {
        List<Category> categories = categoryRepo.getAllMainCategories();
        return convertToDto(categories);
    }


    @Override
    @Cacheable(value = "categories", key = "'sub_' + #categoryId")
    public List<CategoryDto> getCategoryChildren(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("No category with this ID: " + categoryId)
        );

        List<Category> categories = categoryRepo.getCategoryChildren(categoryId);
        return convertToDto(categories);
    }

    private List<CategoryDto> convertToDto(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "categories", key = "'tree'")
    public List<CategoryModelDto> getCategoriesTree() {
        List<Category> categoriesList = categoryRepo.findAll();
        Map<Long, CategoryModel> categoryModelMap = new HashMap<>();
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
                CategoryModelDto categoryModelDto = CategoryMapper.convertToModelDTO(categoryModel);
            finalResult.add(categoryModelDto);
        }

        return finalResult;
    }

}