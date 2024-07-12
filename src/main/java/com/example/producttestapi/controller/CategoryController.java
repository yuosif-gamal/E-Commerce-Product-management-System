package com.example.producttestapi.controller;

import com.example.producttestapi.dto.SuccessResponse;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<SuccessResponse> getAllCategories() {
        List<Category> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(new SuccessResponse("Categories retrieved successfully", true, categories, HttpStatus.OK.value()));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<SuccessResponse> getCategory(@PathVariable("id") int id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(new SuccessResponse("Category retrieved successfully", true, category, HttpStatus.OK.value()));
    }

    @PostMapping("/category")
    public ResponseEntity<SuccessResponse> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Category created successfully", true, createdCategory, HttpStatus.CREATED.value()));
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<SuccessResponse> deleteCategory(@PathVariable("id") int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new SuccessResponse("Category deleted successfully", true, null, HttpStatus.OK.value()));
    }
}
