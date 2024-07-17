package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Category;
import com.example.producttestapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
    @Query(value = "select * from Product p where p.categoryID = :categoryID", nativeQuery = true)
    List<Product> findAllByCategoryID(int categoryID);
}