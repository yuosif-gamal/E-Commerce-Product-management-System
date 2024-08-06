package com.example.producttestapi.controller;

import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.service.VoucherService;
import com.example.producttestapi.dto.SuccessResponse;
import com.example.producttestapi.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vouchers")
@Validated
public class VoucherController {

    private final VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> create(@Valid @RequestBody Voucher voucher) {
        Voucher createdVoucher = voucherService.createVoucher(voucher);
        SuccessResponse response = new SuccessResponse("Voucher created successfully", true, createdVoucher, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{code}")
    public ResponseEntity<SuccessResponse> getVoucher(@PathVariable("code") String code) {
        Voucher voucher = voucherService.findVoucherByCode(code);
        SuccessResponse response = new SuccessResponse("Voucher retrieved successfully", true, voucher, HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteVoucher(@PathVariable int id) {
        voucherService.deleteVoucher(id);
        SuccessResponse response = new SuccessResponse("Voucher deleted successfully", true, null, HttpStatus.NO_CONTENT.value());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

}
