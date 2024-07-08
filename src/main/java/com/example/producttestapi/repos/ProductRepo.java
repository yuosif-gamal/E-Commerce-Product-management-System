package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
    List<Product> findByCategoryID(int categoryID);
}
