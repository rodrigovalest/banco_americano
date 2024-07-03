package com.rodrigovalest.ms_payment.web.dtos.mapper;

import com.rodrigovalest.ms_payment.model.Payment;
import com.rodrigovalest.ms_payment.web.dtos.request.CreatePaymentRequestDto;
import com.rodrigovalest.ms_payment.web.dtos.response.PaymentResponseDto;

public class PaymentMapper {
    public static Payment toEntity(CreatePaymentRequestDto createPaymentRequestDto) {
        return new Payment(
                null,
                createPaymentRequestDto.getCustomerId(),
                createPaymentRequestDto.getCategoryId(),
                createPaymentRequestDto.getTotal(),
                null
        );
    }

    public static PaymentResponseDto toDto(Payment payment) {
        return new PaymentResponseDto(
                payment.getCustomerId(),
                payment.getCategoryId(),
                payment.getTotal(),
                payment.getCreatedDate()
        );
    }
}
