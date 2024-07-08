package com.example.producttestapi.controller;

import com.example.producttestapi.entities.Category;
import com.example.producttestapi.repos.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryRestController {

    @Autowired
    CategoryRepo repo;

    @GetMapping("/categories")
    public List<Category> getAllCategory(){
        return repo.findAll();
    }
    @GetMapping("/category/{id}")
    public Category getCategory(@PathVariable("id") int id){
        return repo.findById(id).get();
    }

    @DeleteMapping("/category/{id}")
    public void deleteCategory(@PathVariable("id") int id){
        repo.deleteById(id);
    }



}
