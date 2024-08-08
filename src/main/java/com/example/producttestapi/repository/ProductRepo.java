package com.example.producttestapi.repository;

import com.example.producttestapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {

    // should ues built-in , but for trying ..
    @Query(value = "SELECT * FROM Product p WHERE p.category_id = :categoryID", nativeQuery = true)
    List<Product> findAllByCategoryID(@Param("categoryID") int categoryID);

    @Query("SELECT p FROM Product p JOIN p.voucherCode v WHERE v.id = :voucherId")
    List<Product> findAllByVoucherID(int voucherId);
}