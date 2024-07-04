package com.rodrigovalest.ms_payment.web.controllers;

import com.rodrigovalest.ms_payment.model.Payment;
import com.rodrigovalest.ms_payment.services.PaymentService;
import com.rodrigovalest.ms_payment.web.dtos.mapper.PaymentMapper;
import com.rodrigovalest.ms_payment.web.dtos.request.CreatePaymentRequestDto;
import com.rodrigovalest.ms_payment.web.dtos.response.PaymentResponseDto;
import com.rodrigovalest.ms_payment.web.exceptions.RestErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Payment", description = "Feature to create, get by id and get by customer payments")
@RestController
@RequestMapping("/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(
            description = "creates a new payment",
            responses = {
                    @ApiResponse(responseCode = "201", description = "CREATED"),
                    @ApiResponse(responseCode = "422", description = "UNPROCESSABLE ENTITY",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreatePaymentRequestDto createPaymentRequestDto) {
        Payment payment = this.paymentService.create(PaymentMapper.toEntity(createPaymentRequestDto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(payment.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(
            description = "find a payment by their id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = " application/json;charset=UTF-8")),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class)))
            })
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> findById(@PathVariable("paymentId") UUID paymentId) {
        Payment payment = this.paymentService.findById(paymentId);
        return ResponseEntity.ok(PaymentMapper.toDto(payment));
    }

    @Operation(
            description = "find payments by customer",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = " application/json;charset=UTF-8")),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class)))
            })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponseDto>> findByCustomerId(@PathVariable("userId") Long userId) {
        List<Payment> paymentList = this.paymentService.findByCustomerId(userId);
        return ResponseEntity.ok(paymentList.stream().map(PaymentMapper::toDto).collect(Collectors.toList()));
    }
}
