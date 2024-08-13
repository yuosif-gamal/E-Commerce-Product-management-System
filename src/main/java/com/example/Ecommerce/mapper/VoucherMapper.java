package com.example.Ecommerce.mapper;

import com.example.Ecommerce.dto.VoucherDto;
import com.example.Ecommerce.entity.Voucher;

public class VoucherMapper {

    public static VoucherDto convertEntityToDto(Voucher voucher) {
        return VoucherDto.builder()
                .code(voucher.getCode())
                .discount(voucher.getDiscount())
                .expiryDate(voucher.getExpiryDate())
                .build();
    }

    public static Voucher convertDtoToEntity(VoucherDto voucherDto) {
        return Voucher.builder()
                .code(voucherDto.getCode())
                .discount(voucherDto.getDiscount())
                .expiryDate(voucherDto.getExpiryDate())
                .build();
    }
}
