package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.VoucherDto;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.Voucher;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.VoucherMapper;
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
    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private VoucherServiceImpl voucherService;

    @Test
    void testFindVoucherByCode_Success() {
        Voucher voucher = Voucher.builder().id(1L).code("code").build();

        when(voucherRepo.findByCode("code")).thenReturn(voucher);

        Voucher result = voucherService.findVoucherByCode("code");

        assertNotNull(result);
        assertEquals("code", result.getCode());
        verify(voucherRepo, times(1)).findByCode("code");
    }

    @Test
    void testFindVoucherByCode_Failed() {
        String code = "code";
        when(voucherRepo.findByCode(code)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            voucherService.findVoucherByCode(code);
        });
        assertEquals("Voucher not found with code: " + code, exception.getMessage());
        verify(voucherRepo, times(1)).findByCode(code);
    }

    @Test
    void testCreateVoucher_Success() {
        LocalDate localDate = LocalDate.of(2025, 1, 1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        VoucherDto voucherDto = VoucherDto.builder().code("code").expiryDate(date).build();

        Voucher voucher = VoucherMapper.convertDtoToEntity(voucherDto);

        when(voucherRepo.findByCode("code")).thenReturn(null);
        when(voucherRepo.save(any(Voucher.class))).thenReturn(voucher);

        VoucherDto result = voucherService.createVoucher(voucherDto);

        assertNotNull(result);
        assertEquals("code", result.getCode());
        assertEquals(date, result.getExpiryDate());
        verify(voucherRepo, times(1)).findByCode("code");
        verify(voucherRepo, times(1)).save(any(Voucher.class)); // Verify with any(Voucher.class)
    }

    @Test
    void testCreateVoucher_ExistCode_ThrowsException() {
        LocalDate localDate = LocalDate.of(2026, 2, 1);
        Date expiredDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        VoucherDto voucherDto = VoucherDto.builder().code("code").expiryDate(expiredDate).build();

        when(voucherRepo.findByCode("code")).thenReturn(VoucherMapper.convertDtoToEntity(voucherDto));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            voucherService.createVoucher(voucherDto);
        });
        assertEquals("Voucher with code already exists: " + voucherDto.getCode(), exception.getMessage());

        verify(voucherRepo, times(1)).findByCode("code");
        verify(voucherRepo, times(0)).save(any(Voucher.class));
    }

    @Test
    void testCreateVoucher_ExpiredDate_ThrowsException() {
        LocalDate localDate = LocalDate.of(2011, 2, 1);
        Date expiredDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        VoucherDto voucherDto = VoucherDto.builder().code("code").expiryDate(expiredDate).build();

        when(voucherRepo.findByCode("code")).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            voucherService.createVoucher(voucherDto);
        });
        assertEquals("Voucher has expired.", exception.getMessage());

        verify(voucherRepo, times(1)).findByCode("code");
        verify(voucherRepo, times(0)).save(any(Voucher.class));
    }

    @Test
    void testAddVoucherToProducts_Success() {
        // Arrange
        Long voucherId = 1L;
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);

        Voucher voucher = Voucher.builder().id(voucherId).code("DISCOUNT10").build();
        Product product1 = Product.builder().id(1L).name("Product 1").build();
        Product product2 = Product.builder().id(2L).name("Product 2").build();
        Product product3 = Product.builder().id(3L).name("Product 3").build();

        List<Product> products = Arrays.asList(product1, product2, product3);

        when(voucherRepo.findById(voucherId)).thenReturn(Optional.of(voucher));
        when(productRepo.findAllById(productIds)).thenReturn(products);

        voucherService.addProductsToVoucher(voucherId, productIds);

        verify(voucherRepo, times(2)).findById(voucherId);
        verify(productRepo, times(1)).findAllById(productIds);
        verify(productRepo, times(1)).saveAll(products);
        assertEquals(voucher, product1.getVoucherCode());
        assertEquals(voucher, product2.getVoucherCode());
        assertEquals(voucher, product3.getVoucherCode());
    }

    @Test
    void testAddVoucherToProducts_Failed_VoucherNotFound() {
        Long voucherId = 1L;
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);

        // Mocking voucherRepo to return an empty voucher, simulating voucher not found
        when(voucherRepo.findById(voucherId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            voucherService.addProductsToVoucher(voucherId, productIds);
        });

        assertEquals("Voucher not found with id: " + voucherId, exception.getMessage());

        verify(voucherRepo, times(1)).findById(voucherId);
        verify(productRepo, times(0)).findAllById(anyList());
        verify(productRepo, times(0)).saveAll(anyList());
    }

    @Test
    void testAddVoucherToProducts_Failed_ProductsNotMatched() {
        Long voucherId = 1L;
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);
        Voucher voucher = Voucher.builder().id(voucherId).code("DISCOUNT10").build();

        Product product1 = Product.builder().id(1L).name("Product 1").build();
        Product product2 = Product.builder().id(2L).name("Product 2").build();
        List<Product> products = Arrays.asList(product1, product2);

        // Mocking voucherRepo to return an empty voucher, simulating voucher not found
        when(voucherRepo.findById(voucherId)).thenReturn(Optional.of(voucher));
        when(productRepo.findAllById(productIds)).thenReturn(products);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            voucherService.addProductsToVoucher(voucherId, productIds);
        });

        assertEquals("One or more product IDs do not match any existing products." , exception.getMessage());

        verify(voucherRepo, times(2)).findById(voucherId);
        verify(productRepo, times(1)).findAllById(anyList());
        verify(productRepo, times(0)).saveAll(anyList());
    }

}
