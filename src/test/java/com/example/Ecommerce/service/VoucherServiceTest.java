package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.VoucherDto;
import com.example.Ecommerce.entity.Voucher;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.VoucherMapper;
import com.example.Ecommerce.repository.VoucherRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceTest {

    @Mock
    private VoucherRepo voucherRepo;

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
}
