package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.CategoryDto;
import com.example.Ecommerce.dto.SuccessResponse;
import com.example.Ecommerce.entity.Category;
import com.example.Ecommerce.dto.CategoryModelDto;
import com.example.Ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/main")
    @Operation(summary = "Get Main Categories", description = "Retrieve all main categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Main categories retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getAllMainCategories() {
        List<CategoryDto> categories = categoryService.getAllMainCategories();
        return ResponseEntity.ok(new SuccessResponse("Main Categories retrieved successfully", categories, HttpStatus.OK.value()));
    }

    @GetMapping
    @Operation(summary = "Get All Categories", description = "Retrieve all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All categories retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(new SuccessResponse("ALL Categories retrieved successfully", categories, HttpStatus.OK.value()));
    }

    @GetMapping("/tree")
    @Operation(summary = "Get Categories Tree", description = "Retrieve a hierarchical tree of categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories tree retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getCategoriesTree() {
        List<CategoryModelDto> categories = categoryService.getCategoriesTree();
        return ResponseEntity.ok(new SuccessResponse("All Categories Tree retrieved successfully", categories, HttpStatus.OK.value()));
    }

    @GetMapping("/{id}/children")
    @Operation(summary = "Get Category Children", description = "Retrieve child categories of a given category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Child categories retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getCategoryChildren(@PathVariable Long id) {
        List<CategoryDto> categories = categoryService.getCategoryChildren(id);
        return ResponseEntity.ok(new SuccessResponse("Children Categories retrieved successfully", categories, HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Category", description = "Retrieve details of a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getCategory(@PathVariable("id") Long id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(new SuccessResponse("Category retrieved successfully", category, HttpStatus.OK.value()));
    }

    @PostMapping
    @Operation(summary = "Create Category", description = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category details provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> createCategory(@Valid @RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Category created successfully", createdCategory, HttpStatus.CREATED.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Category", description = "Delete a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new SuccessResponse("Category deleted successfully", null, HttpStatus.OK.value()));
    }
}
