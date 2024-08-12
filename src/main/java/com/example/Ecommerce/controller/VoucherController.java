package com.example.Ecommerce.controller;

import com.example.Ecommerce.entity.Voucher;
import com.example.Ecommerce.service.VoucherService;
import com.example.Ecommerce.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vouchers")
@Validated
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;
    private final static Logger LOGGER = LoggerFactory.getLogger(VoucherController.class);

    @PostMapping
    @Operation(summary = "Create a new voucher", description = "Creates a new voucher with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voucher created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<SuccessResponse> create(
            @Valid @RequestBody Voucher voucher) {
        LOGGER.info("Received request to create a new voucher with details: {}", voucher);
        Voucher createdVoucher = voucherService.createVoucher(voucher);
        LOGGER.info("Voucher created with ID {}", createdVoucher.getId());
        SuccessResponse response = new SuccessResponse("Voucher created successfully", createdVoucher, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{code}")
    @Operation(summary = "Retrieve a voucher by code", description = "Fetches the details of a voucher using its code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voucher retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Voucher not found")
    })
    public ResponseEntity<SuccessResponse> getVoucher(
            @PathVariable("code") String code) {
        LOGGER.info("Received request to get voucher with code {}", code);
        Voucher voucher = voucherService.findVoucherByCode(code);
        LOGGER.info("Returning voucher with code {}", code);
        SuccessResponse response = new SuccessResponse("Voucher retrieved successfully", voucher, HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a voucher by ID", description = "Deletes a voucher based on its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Voucher deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Voucher not found")
    })
    public ResponseEntity<SuccessResponse> deleteVoucher(
            @PathVariable("id") Long id) {
        LOGGER.info("Received request to delete voucher with ID {}", id);
        voucherService.deleteVoucher(id);
        LOGGER.info("Voucher with ID {} deleted successfully", id);
        SuccessResponse response = new SuccessResponse("Voucher deleted successfully", null, HttpStatus.NO_CONTENT.value());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
