package com.example.Ecommerce.repository;

import com.example.Ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.parentCategory IS null")
    List<Category> getAllMainCategories();

    @Query("SELECT c FROM Category c WHERE c.parentCategory.id = :categoryId")
    List<Category> getCategoryChildren(@Param("categoryId") Long categoryId);
}
