package com.example.Ecommerce.repository;

import com.example.Ecommerce.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE product SET voucher_code = null WHERE voucher_code = :voucherId", nativeQuery = true)
    void updateProductsWithoutVoucher(Long voucherId);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);


}