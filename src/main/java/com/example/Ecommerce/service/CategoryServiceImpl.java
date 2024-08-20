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

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepo categoryRepo;

    @Cacheable(value = "categories", key = "'all'")
    @Override
    public List<CategoryDto> getAllCategory() {
        LOGGER.info("Fetching all categories from the database.");

        List<Category> categories = categoryRepo.findAll();
        List<CategoryDto> categoryDto = convertToDto(categories);

        LOGGER.info("Successfully fetched {} categories.", categoryDto.size());
        return categoryDto;
    }

    @Override
    @Cacheable(value = "categories", key = "#id")
    public CategoryDto getCategory(Long id) {
        LOGGER.info("Fetching category with ID: {}", id);

        Category category = categoryRepo.findById(id).orElse(null);
        if (category == null) {
            LOGGER.error("Category not found with ID: {}", id);
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        CategoryDto categoryDto = CategoryMapper.convertEntityToDto(category);

        LOGGER.info("Category found and converted to DTO: {}", categoryDto);
        return categoryDto;
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public Category createCategory(Category category) {
        LOGGER.info("Creating new category: {}", category);

        Category createdCategory = categoryRepo.save(category);

        LOGGER.info("Category created with ID: {}", createdCategory.getId());
        return createdCategory;
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(Long id) {
        LOGGER.info("Deleting category with ID: {}", id);

        Category category = categoryRepo.findById(id).orElse(null);
        if (category == null) {
            LOGGER.error("Category not found with ID: {}", id);
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepo.deleteById(id);

        LOGGER.info("Category with ID: {} deleted successfully.", id);
    }

    @Override
    @Cacheable(value = "categories", key = "'main'")
    public List<CategoryDto> getAllMainCategories() {
        LOGGER.info("Fetching all main categories.");

        List<Category> categories = categoryRepo.findByParentCategoryIsNull();
        List<CategoryDto> categoryDto = convertToDto(categories);

        LOGGER.info("Successfully fetched {} main categories.", categoryDto.size());
        return categoryDto;
    }

    @Override
    @Cacheable(value = "categories", key = "'sub_' + #categoryId")
    public List<CategoryDto> getCategoryChildren(Long categoryId) {
        LOGGER.info("Fetching children categories for category ID: {}", categoryId);

        Category category = categoryRepo.findById(categoryId).orElse(null);
        if (category == null) {
            LOGGER.error("Category not found with ID: {}", categoryId);
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        List<Category> categories = categoryRepo.findByParentCategoryId(categoryId);
        List<CategoryDto> categoryDto = convertToDto(categories);

        LOGGER.info("Successfully fetched {} child categories for parent ID: {}", categoryDto.size(), categoryId);
        return categoryDto;
    }

    private List<CategoryDto> convertToDto(List<Category> categories) {
        LOGGER.debug("Converting {} categories to DTOs.", categories.size());
        return categories.stream()
                .map(CategoryMapper::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "categories", key = "'tree'")
    public List<CategoryModelDto> getCategoriesTree() {
        LOGGER.info("Building category tree.");

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

        List<CategoryModelDto> finalResult = result.stream()
                .map(CategoryMapper::convertToModelDTO)
                .collect(Collectors.toList());

        LOGGER.info("Successfully built category tree with {} root categories.", finalResult.size());
        return finalResult;
    }

}
