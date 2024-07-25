package com.example.producttestapi.service;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.CategoryRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Test
    public void CategoryService_CreateCategory_ReturnCategory(){
        Category category = Category.builder().name("elec").build();
        when(categoryRepo.save(Mockito.any(Category.class))).thenReturn(category);

        Category savedCategory = categoryService.createCategory(category);

        Assertions.assertThat(savedCategory).isNotNull();
    }

    @Test
    public void CategoryService_GetCategory_ReturnCategoryById(){
        Category category = Category.builder().name("elec").build();

        when(categoryRepo.findById(category.getId())).thenReturn(Optional.of(category));

        Category category1 =categoryService.getCategory(category.getId());

        Assertions.assertThat(category1).isNotNull();
        Assertions.assertThat(category1.getName()).isEqualTo("elec");

    }

    @Test
    public void CategoryService_GetMainCategories_ReturnMainCategories(){
        Category category1 = Category.builder().name("TVs").build();
        Category category2 = Category.builder().name("phones").build();

        when(categoryRepo.getAllMainCategories()).thenReturn(List.of(category1,category2));

        List<CategoryDto> categories =categoryService.getAllMainCategories();

        Assertions.assertThat(categories).isNotNull();
        Assertions.assertThat(categories.size()).isEqualTo(2);

    }

    @Test
    public void CategoryService_GetSubCategories_ReturnSubCategories(){
        Category category1 = Category.builder().name("TVs").id(1).build();
        Category category3 = Category.builder().name("phones1").id(2).parentCategory(category1).build();
        Category category4 = Category.builder().name("phones2").id(3).parentCategory(category1).build();

        when(categoryRepo.findById(category1.getId())).thenReturn(Optional.of(category1));
        when(categoryRepo.getCategoryChildren(category1.getId())).thenReturn(List.of(category3, category4));

        List<CategoryDto> categories = categoryService.getCategoryChildren(category1.getId());

        Assertions.assertThat(categories).isNotNull();
        Assertions.assertThat(categories.size()).isEqualTo(2);
        Assertions.assertThat(categories.get(0).getName()).isEqualTo("phones1");
        Assertions.assertThat(categories.get(1).getName()).isEqualTo("phones2");
    }

    @Test
    public void CategoryService_DeleteCategoryById_ReturnVoid() {
        int categoryId = 1;
        when(categoryRepo.existsById(categoryId)).thenReturn(true);
        doNothing().when(categoryRepo).deleteById(categoryId);

        categoryService.deleteCategory(categoryId);

        verify(categoryRepo, times(1)).existsById(categoryId);
        verify(categoryRepo, times(1)).deleteById(categoryId);
    }

    @Test
    public void CategoryService_DeleteCategoryById_ThrowsExceptionWhenCategoryNotFound() {

        int categoryId = 1;
        when(categoryRepo.existsById(categoryId)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> categoryService.deleteCategory(categoryId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with id: " + categoryId);

        verify(categoryRepo, times(1)).existsById(categoryId);
        verify(categoryRepo, times(0)).deleteById(categoryId);
    }

}
