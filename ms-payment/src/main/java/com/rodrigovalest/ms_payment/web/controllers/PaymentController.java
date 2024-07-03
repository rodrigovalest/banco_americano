package com.rodrigovalest.ms_payment.web.controllers;

import com.rodrigovalest.ms_payment.model.Payment;
import com.rodrigovalest.ms_payment.services.PaymentService;
import com.rodrigovalest.ms_payment.web.dtos.mapper.PaymentMapper;
import com.rodrigovalest.ms_payment.web.dtos.request.CreatePaymentRequestDto;
import com.rodrigovalest.ms_payment.web.dtos.response.PaymentResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreatePaymentRequestDto createPaymentRequestDto) {
        Payment payment = this.paymentService.create(PaymentMapper.toEntity(createPaymentRequestDto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(payment.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> findById(@PathVariable("paymentId") UUID paymentId) {
        Payment payment = this.paymentService.findById(paymentId);
        return ResponseEntity.ok(PaymentMapper.toDto(payment));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponseDto>> findByCustomerId(@PathVariable("userId") Long userId) {
        List<Payment> paymentList = this.paymentService.findByCustomerId(userId);
        return ResponseEntity.ok(paymentList.stream().map(PaymentMapper::toDto).collect(Collectors.toList()));
    }
}
