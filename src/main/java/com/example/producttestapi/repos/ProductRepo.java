package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Category;
import com.example.producttestapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {
    @Query(value = "SELECT * FROM Product p WHERE p.category_id = :categoryID", nativeQuery = true)
    List<Product> findAllByCategoryID(@Param("categoryID") int categoryID);

    @Query("SELECT p FROM Product p JOIN p.voucherCode v WHERE v.id = :voucherId")
    List<Product> findAllByVoucherID(int voucherId);
}