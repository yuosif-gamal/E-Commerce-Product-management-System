package com.example.Ecommerce.repository;

import com.example.Ecommerce.entity.Voucher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@DataJpaTest
public class VoucherRepositoryTests {
    @Autowired
    private VoucherRepo voucherRepo;

    @Test
    public void VoucherRepo_SaveVoucher_ReturnSavedCategory(){
        Date date = new Date(2025,11,12);
        Voucher voucher = Voucher.builder().discount(BigDecimal.valueOf(10)).expireDate(date).code("SALE").build();

        Voucher v = voucherRepo.save(voucher);

        Assertions.assertThat(v).isNotNull();
        Assertions.assertThat(v.getCode()).isEqualTo("SALE");
        Assertions.assertThat(v.getId()).isGreaterThan(0);

    }

    @Test
    public void VoucherRepo_SaveAllVoucher_ReturnAllSavedVoucher(){
        Date date1 = new Date(2025,11,12);
        Voucher voucher1 = Voucher.builder().discount(BigDecimal.valueOf(10)).expireDate(date1).code("SALE1").build();

        Date date2 = new Date(2025,11,12);
        Voucher voucher2 = Voucher.builder().discount(BigDecimal.valueOf(10)).expireDate(date2).code("SALE2").build();

        voucherRepo.save(voucher1);
        voucherRepo.save(voucher2);

        List<Voucher> vouchersList =voucherRepo.findAll();

        Assertions.assertThat(vouchersList).isNotNull();
        Assertions.assertThat(vouchersList.size()).isEqualTo(2);
    }

    @Test
    public void VoucherRepo_FindVoucherByCode_ReturnVoucherByCode(){
        Date date = new Date(2025,11,12);
        Voucher voucher = Voucher.builder().discount(BigDecimal.valueOf(10)).expireDate(date).code("SALE1").build();

        voucherRepo.save(voucher);

        Voucher SavedVoucher = voucherRepo.findByCode(voucher.getCode());

        Assertions.assertThat(SavedVoucher.getCode()).isEqualTo("SALE1");
    }




}
