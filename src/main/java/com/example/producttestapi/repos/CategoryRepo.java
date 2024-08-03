package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.parentCategory IS null ")
    List<Category> getAllMainCategories();
    @Query("SELECT c FROM Category c WHERE c.parentCategory.id = :categoryId")
    List<Category>getCategoryChildren(Integer categoryId);
}