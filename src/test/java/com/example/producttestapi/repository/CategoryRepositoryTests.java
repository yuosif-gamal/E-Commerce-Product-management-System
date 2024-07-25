package com.example.producttestapi.repository;

import com.example.producttestapi.entities.Category;
import com.example.producttestapi.repos.CategoryRepo;
import jakarta.persistence.Table;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepo categoryRepo;
    @Test
    public void CategoryRepo_SaveCategory_ReturnSavedCategory(){
        Category category = Category.builder().name("elec").build();

        Category saveCategory = categoryRepo.save(category);

        Assertions.assertThat(saveCategory).isNotNull();
        Assertions.assertThat(saveCategory.getId()).isGreaterThan(0);

    }

    @Test
    public void CategoryRepo_SaveAllCategory_ReturnAllSavedCategory(){
        Category category1 = Category.builder().name("elec").build();
        Category category2 = Category.builder().name("samsung").build();

        categoryRepo.save(category1);
        categoryRepo.save(category2);

        List<Category> categoryList =categoryRepo.findAll();

        Assertions.assertThat(categoryList).isNotNull();
        Assertions.assertThat(categoryList.size()).isEqualTo(2);

    }

    @Test
    public void CategoryRepo_getCategoryById_ReturnCategory(){
        Category category1 = Category.builder().name("elec").build();
        Category category2 = Category.builder().name("samsung").build();

        categoryRepo.save(category1);
        categoryRepo.save(category2);
        int id = category1.getId();

        Category category = categoryRepo.findById(id).get();

        Assertions.assertThat(category).isNotNull();
        Assertions.assertThat(category.getName()).isEqualTo("elec");
    }


    @Test
    public void CategoryRepo_DeleteCategoryById_ReturnNullCategory(){
        Category category1 = Category.builder().name("elec").build();
        Category category2 = Category.builder().name("samsung").build();

        categoryRepo.save(category1);
        categoryRepo.save(category2);
        int id = category1.getId();
        categoryRepo.deleteById(id);

        Category category = categoryRepo.findById(id).orElse(null);
        Assertions.assertThat(category).isNull();
        List<Category> categoryList =categoryRepo.findAll();
        Assertions.assertThat(categoryList.size()).isEqualTo(1);


    }

    @Test
    public void CategoryRepo_GetMainCategories_ReturnAllMainCategories(){
        Category category1 = Category.builder().name("elec").build();
        Category category2 = Category.builder().name("samsung").build();
        Category category3 = Category.builder().name("samsung1").build();
        Category category4 = Category.builder().name("samsung2").parentCategory(category3).build();

        categoryRepo.save(category1);
        categoryRepo.save(category2);
        categoryRepo.save(category3);
        categoryRepo.save(category4);


        List<Category> categoryMainList =categoryRepo.getAllMainCategories();
        List<Category> categoryList =categoryRepo.findAll();
        Assertions.assertThat(categoryList.size()).isEqualTo(4);
        int categoryListSize = categoryList.size();
        Assertions.assertThat(categoryMainList.size()).isLessThan(categoryListSize);

    }

    @Test
    public void CategoryRepo_GetChildrenCategories_ReturnAllChildrenCategories(){
        Category category2 = Category.builder().name("samsung").build();
        Category category3 = Category.builder().name("samsung1").build();
        Category category4 = Category.builder().name("samsung2").parentCategory(category3).build();

        categoryRepo.save(category2);
        categoryRepo.save(category3);
        categoryRepo.save(category4);


        List<Category> categoryChildrenList =categoryRepo.getCategoryChildren(category3.getId());
        Assertions.assertThat(categoryChildrenList.size()).isEqualTo(1);

        categoryChildrenList =categoryRepo.getCategoryChildren(category4.getId());
        Assertions.assertThat(categoryChildrenList.size()).isEqualTo(0);

    }
}
