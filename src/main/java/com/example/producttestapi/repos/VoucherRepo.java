package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepo extends JpaRepository<Voucher, Integer> {
    Voucher findByCode(String code);
}
