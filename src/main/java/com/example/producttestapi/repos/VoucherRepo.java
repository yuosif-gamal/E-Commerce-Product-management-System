package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepo extends JpaRepository<Voucher, Long> {
    Voucher findByCode(String code);
}
