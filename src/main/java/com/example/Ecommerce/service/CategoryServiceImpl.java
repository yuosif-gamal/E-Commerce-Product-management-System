package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.CategoryDto;
import com.example.Ecommerce.entity.Category;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.CategoryMapper;
import com.example.Ecommerce.model.CategoryModel;
import com.example.Ecommerce.dto.CategoryModelDto;
import com.example.Ecommerce.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableCaching
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepo categoryRepo;

    @Cacheable(value = "categories", key = "'all'")
    @Override
    public List<CategoryDto> getAllCategory() {

        List<Category> categories = categoryRepo.findAll();

        return convertToDto(categories);
    }

    @Override
    @Cacheable(value = "categories", key = "#id")
    public CategoryDto getCategory(Long id) {

        Category category = categoryRepo.findById(id).orElse(null);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }

        return CategoryMapper.convertEntityToDto(category);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(Long id) {

        Category category = categoryRepo.findById(id).orElse(null);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepo.deleteById(id);

    }

    @Override
    @Cacheable(value = "categories", key = "'main'")
    public List<CategoryDto> getAllMainCategories() {

        List<Category> categories = categoryRepo.findByParentCategoryIsNull();

        return convertToDto(categories);
    }

    @Override
    @Cacheable(value = "categories", key = "'sub_' + #categoryId")
    public List<CategoryDto> getCategoryChildren(Long categoryId) {

        Category category = categoryRepo.findById(categoryId).orElse(null);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        List<Category> categories = categoryRepo.findByParentCategoryId(categoryId);

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

        return result.stream()
                .map(CategoryMapper::convertToModelDTO)
                .collect(Collectors.toList());
    }

}
