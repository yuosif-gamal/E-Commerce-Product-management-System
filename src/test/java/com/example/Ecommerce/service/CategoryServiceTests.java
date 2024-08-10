package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.CategoryDto;
import com.example.Ecommerce.entity.Category;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.CategoryMapper;
import com.example.Ecommerce.repository.CategoryRepo;
import net.bytebuddy.asm.Advice;
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
        assertEquals("first category", result.getName());
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
        assertEquals("Category not found with id: " + categoryID, thrownException.getMessage());
        verify(categoryRepo, times(1)).findById(categoryID);
    }

    @Test
    void testGetAllCategories_Success() {
        // Arrange -> set up data
        List<Category> allCategories = new ArrayList<>();
        Category category = new Category();

        Long firstCategoryID = 1L;
        String firstCategoryName = "first category";
        category.setId(firstCategoryID);
        category.setName(firstCategoryName);

        allCategories.add(category);

        firstCategoryID = 2L;
        firstCategoryName = "second category";
        category.setId(firstCategoryID);
        category.setName(firstCategoryName);

        allCategories.add(category);

        // Arrange -> Mock behavior (simulate )
        when(categoryRepo.findAll()).thenReturn(allCategories);

        // Act
        List<CategoryDto> result = categoryService.getAllCategory();

        //Assert
        assertNotNull(result);
        assertEquals(result.size() , 2);
        assertEquals(result.get(0).getName() , allCategories.get(0).getName());
        verify(categoryRepo,times(1)).findAll();
    }

    @Test
    void testGetAllCategories_ReturnEmptyList(){
        List<Category> allCategories = new ArrayList<>();

        when(categoryRepo.findAll()).thenReturn(allCategories);

        // Act
        List<CategoryDto> result = categoryService.getAllCategory();

        //Assert
        assertEquals(result.size() , 0);
        verify(categoryRepo,times(1)).findAll();

    }

}
