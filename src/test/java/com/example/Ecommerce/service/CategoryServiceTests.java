package com.example.Ecommerce.service;

import com.example.Ecommerce.config.CacheConfig;
import com.example.Ecommerce.dto.CategoryDto;
import com.example.Ecommerce.entity.Category;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.repository.CategoryRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class CategoryServiceTests {
    @Mock
    private CategoryRepo categoryRepo;
    @InjectMocks
    private CategoryServiceImpl categoryService;


    @Test
    void testGetCategory_Success() {
        // Arrange
        Long categoryID = 1L;
        String categoryName = "first category";

        Category category = new Category();
        category.setId(categoryID);
        category.setName(categoryName);

        // Mock behavior
        when(categoryRepo.findById(categoryID)).thenReturn(Optional.of(category));

        // Act
        CategoryDto result = categoryService.getCategory(categoryID);

        // Assert
        assertNotNull(result);
        assertEquals(result.getName(), "first category");
        verify(categoryRepo, times(1)).findById(categoryID);
    }

    @Test
    void testGetCategory_Failed() {
        // Arrange -> prepare data
        Long categoryID = 1L;

        // Arrange -> Mock behavior
        when(categoryRepo.findById(categoryID)).thenReturn(Optional.empty());

        // Act that there is a Exception
        ResourceNotFoundException thrownException = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategory(categoryID);
        });

        // Assert that the exception message is correct
        assertEquals(thrownException.getMessage(), "Category not found with id: " + categoryID);
        verify(categoryRepo, times(1)).findById(categoryID);
    }

    @Test
    void testGetAllCategories_Success() {
        // Arrange -> set up data
        List<Category> allCategories = new ArrayList<>();
        Category category = new Category();

        String firstCategoryName = "first category";
        category.setName(firstCategoryName);

        allCategories.add(category);

        firstCategoryName = "second category";
        category.setName(firstCategoryName);

        allCategories.add(category);

        // Arrange -> Mock behavior (simulate )
        when(categoryRepo.findAll()).thenReturn(allCategories);

        // Act
        List<CategoryDto> result = categoryService.getAllCategory();

        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepo, times(1)).findAll();
    }

    @Test
    void testGetAllCategories_ReturnEmptyList() {
        List<Category> allCategories = new ArrayList<>();

        when(categoryRepo.findAll()).thenReturn(allCategories);

        // Act
        List<CategoryDto> result = categoryService.getAllCategory();

        //Assert
        assertEquals(0, result.size());
        verify(categoryRepo, times(1)).findAll();

    }

    @Test
    void testCreateCategory() {
        String categoryName = "first category";
        Category category = new Category();
        category.setName(categoryName);

        when(categoryRepo.save(category)).thenReturn(category);

        Category result = categoryService.createCategory(category);

        assertEquals(category, result);
        assertEquals(categoryName, result.getName());
        assertEquals(null, result.getSubCategories());
        verify(categoryRepo, times(1)).save(category);

    }

    @Test
    void testDeleteCategory_Success() {
        String categoryName = "first category";
        Long categoryID = 1L;
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryID);

        doNothing().when(categoryRepo).deleteById(categoryID);
        when(categoryRepo.findById(categoryID)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(categoryID);
        verify(categoryRepo, times(1)).deleteById(categoryID);
        verify(categoryRepo, times(1)).findById(categoryID);

    }

    @Test
    void testDeleteCategory_Failed() {
        Long categoryID = 1L;

        when(categoryRepo.findById(categoryID)).thenReturn(Optional.empty());

        ResourceNotFoundException thrownException = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.deleteCategory(categoryID);
        });

        assertEquals(thrownException.getMessage(), "Category not found with id: " + categoryID);
        verify(categoryRepo, times(0)).deleteById(categoryID);
        verify(categoryRepo, times(1)).findById(categoryID);

    }

    @Test
    void testGetAllMainCategories() {
        Category firstCategory = new Category();
        firstCategory.setName("first_category");

        Category secondCategory = new Category();
        secondCategory.setName("second_category");

        List<Category> categories = new ArrayList<>();
        categories.add(firstCategory);
        categories.add(secondCategory);

        when(categoryRepo.findByParentCategoryIsNull()).thenReturn(categories);

        List<CategoryDto> result = categoryService.getAllMainCategories();
        List<CategoryDto> result2 = categoryService.getAllMainCategories();

        assertEquals(2, result.size());
        assertEquals("first_category", result.get(0).getName());
        assertEquals("second_category", result.get(1).getName());
        verify(categoryRepo, times(2)).findByParentCategoryIsNull();
    }

    @Test
    void testGetAllMainCategories_ReturnEmptyList() {
        List<Category> categories = new ArrayList<>();

        when(categoryRepo.findByParentCategoryIsNull()).thenReturn(categories);

        List<CategoryDto> result = categoryService.getAllMainCategories();

        assertEquals(0, result.size());
        verify(categoryRepo, times(1)).findByParentCategoryIsNull();
    }

    @Test
    void testGetCategoryChildren_Success() {
        Category mainCategory = new Category();
        mainCategory.setName("Main_category");
        mainCategory.setId(1L);

        Category firstCategory = new Category();
        firstCategory.setName("first_category");
        firstCategory.setParentCategory(mainCategory);

        Category secondCategory = new Category();
        secondCategory.setName("second_category");
        secondCategory.setParentCategory(mainCategory);

        List<Category> categories = new ArrayList<>();
        categories.add(firstCategory);
        categories.add(secondCategory);

        when(categoryRepo.findById(mainCategory.getId())).thenReturn(Optional.of(mainCategory));
        when(categoryRepo.findByParentCategoryId(mainCategory.getId())).thenReturn(categories);

        List<CategoryDto> result = categoryService.getCategoryChildren(mainCategory.getId());

        assertEquals(2, result.size());
        assertEquals(1, categories.get(0).getParentCategory().getId());
        assertEquals(1, categories.get(1).getParentCategory().getId());
        verify(categoryRepo, times(1)).findByParentCategoryId(mainCategory.getId());
        verify(categoryRepo, times(1)).findById(mainCategory.getId());
    }


    @Test
    void testGetCategoryChildren_Failed() {

        List<Category> categories = new ArrayList<>();
        Long id = 1L;

        when(categoryRepo.findById(id)).thenReturn(Optional.empty());


        ResourceNotFoundException thrownException = assertThrows(ResourceNotFoundException.class,() -> {
           categoryService.getCategoryChildren(id);
        });


        assertEquals(thrownException.getMessage(), "Category not found with id: " + id);
        verify(categoryRepo, times(0)).findByParentCategoryId(id);
        verify(categoryRepo, times(1)).findById(id);

    }

}
