package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.Voucher;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.repository.ProductRepo;
import com.example.Ecommerce.repository.VoucherRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceTest {
    @Mock
    private VoucherRepo voucherRepo;

    @InjectMocks
    private VoucherServiceImpl voucherService;

    @Test
    void testFindVoucherByCode_Success(){
        Voucher voucher = Voucher.builder().id(1L).code("code").build();

        when(voucherRepo.findByCode("code")).thenReturn(voucher);

        Voucher result = voucherService.findVoucherByCode("code");

        assertNotNull(result);
        assertEquals("code", result.getCode());
        verify(voucherRepo , times(1)).findByCode("code");
    }

    @Test
    void testFindVoucherByCode_Failed(){
        String code = "code";
        when(voucherRepo.findByCode(code)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,() -> {
            voucherService.findVoucherByCode(code);
        });
        assertEquals("Voucher not found with code: " + code,  exception.getMessage());
        verify(voucherRepo , times(1)).findByCode(code);
    }

    @Test
    void testCreateVoucher_Success(){
        Date date = new Date(2025,1,1);
        Voucher voucher = Voucher.builder().id(1L).code("code").expireDate(date).build();

        when(voucherRepo.findByCode("code")).thenReturn(null);
        when(voucherRepo.save(voucher)).thenReturn(voucher);

        Voucher result = voucherService.createVoucher(voucher);

        assertEquals(date , result.getExpireDate());
        verify(voucherRepo,times(1)).findByCode("code");
        verify(voucherRepo,times(1)).save(voucher);
    }

    @Test
    void testCreateVoucher_ExistCode_ThrowsException(){
        LocalDate localDate = LocalDate.of(2026, 2, 1);
        Date expiredDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Voucher voucher = Voucher.builder().id(1L).code("code").expireDate(expiredDate).build();

        when(voucherRepo.findByCode("code")).thenReturn(voucher);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            voucherService.createVoucher(voucher);
        });
        assertEquals("Voucher with code already exists: " + voucher.getCode(), exception.getMessage());

        verify(voucherRepo, times(1)).findByCode("code");
        verify(voucherRepo, times(0)).save(any(Voucher.class));
    }

    @Test
    void testCreateVoucher_ExpiredDate_ThrowsException() {
        LocalDate localDate = LocalDate.of(2011, 2, 1);
        Date expiredDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Voucher voucher = Voucher.builder().id(1L).code("code").expireDate(expiredDate).build();

        when(voucherRepo.findByCode("code")).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            voucherService.createVoucher(voucher);
        });
        assertEquals("Voucher has expired.", exception.getMessage());

        verify(voucherRepo, times(1)).findByCode("code");
        verify(voucherRepo, times(0)).save(any(Voucher.class));
    }




}
