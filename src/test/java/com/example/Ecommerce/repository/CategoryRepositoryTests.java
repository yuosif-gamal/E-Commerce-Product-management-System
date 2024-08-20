package com.example.Ecommerce.repository;

import com.example.Ecommerce.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepo categoryRepo;

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


        List<Category> categoryMainList =categoryRepo.findByParentCategoryIsNull();
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


        List<Category> categoryChildrenList =categoryRepo.findByParentCategoryId(category3.getId());
        Assertions.assertThat(categoryChildrenList.size()).isEqualTo(1);

        categoryChildrenList =categoryRepo.findByParentCategoryId(category4.getId());
        Assertions.assertThat(categoryChildrenList.size()).isEqualTo(0);

    }
}
