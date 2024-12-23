package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.CategoryDto;
import com.example.Ecommerce.dto.SuccessResponse;
import com.example.Ecommerce.dto.CategoryModelDto;
import com.example.Ecommerce.entity.Category;
import com.example.Ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Validated
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final static Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping("/main")
    @Operation(summary = "Get Main Categories", description = "Retrieve all main categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Main categories retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getAllMainCategories() {
        LOGGER.info("Received request to get all main categories");
        List<CategoryDto> categories = categoryService.getAllMainCategories();
        LOGGER.info("Returning {} main categories", categories.size());
        return ResponseEntity.ok(new SuccessResponse("Main Categories retrieved successfully", categories, HttpStatus.OK.value()));
    }

    @GetMapping
    @Operation(summary = "Get All Categories", description = "Retrieve all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All categories retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getAllCategories() {
        LOGGER.info("Received request to get all categories");
        List<CategoryDto> categories = categoryService.getAllCategory();
        LOGGER.info("Returning {} categories", categories.size());
        return ResponseEntity.ok(new SuccessResponse("ALL Categories retrieved successfully", categories, HttpStatus.OK.value()));
    }

    @GetMapping("/tree")
    @Operation(summary = "Get Categories Tree", description = "Retrieve a hierarchical tree of categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories tree retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SuccessResponse> getCategoriesTree() {
        LOGGER.info("Received request to get categories tree");
        List<CategoryModelDto> categories = categoryService.getCategoriesTree();
        LOGGER.info("Returning categories tree with {} nodes", categories.size());
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
        LOGGER.info("Received request to get children of category ID {}", id);
        List<CategoryDto> categories = categoryService.getCategoryChildren(id);
        LOGGER.info("Returning {} children categories for category ID {}", categories.size(), id);
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
        LOGGER.info("Received request to get category ID {}", id);
        CategoryDto category = categoryService.getCategory(id);
        LOGGER.info("Returning details for category ID {}", id);
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
        LOGGER.info("Received request to create a new category with details: {}", category);
        Category createdCategory = categoryService.createCategory(category);
        LOGGER.info("Category created with ID {}", createdCategory.getId());
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
        LOGGER.info("Received request to delete category ID {}", id);
        categoryService.deleteCategory(id);
        LOGGER.info("Category ID {} deleted successfully", id);
        return ResponseEntity.ok(new SuccessResponse("Category deleted successfully", null, HttpStatus.OK.value()));
    }
}
