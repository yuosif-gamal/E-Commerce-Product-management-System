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

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    @Autowired
    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Cacheable(value = "categories")
    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepo.findAll();
        List<CategoryDto> categoryDto = new ArrayList<>();
        for (Category category1 : categories ){
            CategoryDto c = CategoryMapper.convertEntityToDto(category1);
            categoryDto.add(c);
        }
        return categoryDto;
    }

    @Override
    @Cacheable(value = "findCategory",key = "#id")
    public Category getCategory(int id) {
        Category category = categoryRepo.findById(id).orElse(null);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        return category;
    }
    @Override
    @Caching(evict = {
            @CacheEvict(value = "categories", allEntries = true),
            @CacheEvict(value = "mainCategories", allEntries = true),
            @CacheEvict(value = "categoryTree", allEntries = true),
            @CacheEvict(value = "subCategories", allEntries = true),
            @CacheEvict(value = "findCategory",allEntries = true)
    })
    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "categories", allEntries = true),
                    @CacheEvict(value = "categoryById", key = "#id"),
                    @CacheEvict(value = "mainCategories", allEntries = true),
                    @CacheEvict(value = "categoryTree", allEntries = true),
                    @CacheEvict(value = "subCategories", allEntries = true),
                    @CacheEvict(value = "findCategory", key = "#id")
            }
    )

    public void deleteCategory(int id) {
        if (!categoryRepo.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepo.deleteById(id);
    }

    @Override
    @Cacheable(value = "mainCategories")
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
    @Cacheable(value = "subCategories")
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
    @Cacheable(value = "categoryTree")
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