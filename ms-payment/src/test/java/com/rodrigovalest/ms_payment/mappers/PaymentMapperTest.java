package com.rodrigovalest.ms_payment.mappers;

import com.rodrigovalest.ms_payment.model.Payment;
import com.rodrigovalest.ms_payment.web.dtos.mapper.PaymentMapper;
import com.rodrigovalest.ms_payment.web.dtos.request.CreatePaymentRequestDto;
import com.rodrigovalest.ms_payment.web.dtos.response.PaymentResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentMapperTest {

    @Test
    public void toEntityFromCreatePaymentRequestDto_WithValidData_ReturnsPayment() {
        // Arrange
        CreatePaymentRequestDto createPaymentRequestDto = new CreatePaymentRequestDto(1L, 100L, 10000L);

        // Act
        Payment sut = PaymentMapper.toEntity(createPaymentRequestDto);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isNull();
        Assertions.assertThat(sut.getCategoryId()).isEqualTo(createPaymentRequestDto.getCategoryId());
        Assertions.assertThat(sut.getCustomerId()).isEqualTo(createPaymentRequestDto.getCustomerId());
        Assertions.assertThat(sut.getTotal()).isEqualTo(createPaymentRequestDto.getTotal());
        Assertions.assertThat(sut.getCreatedDate()).isNull();
    }

    @Test
    public void toDto_WithValidData_ReturnsPaymentResponseDto() {
        // Arrange
        Payment payment = new Payment(UUID.randomUUID(), 1L, 100L, 10000L, LocalDateTime.of(2024, 1, 15, 10, 30));

        // Act
        PaymentResponseDto sut = PaymentMapper.toDto(payment);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getCategoryId()).isEqualTo(payment.getCategoryId());
        Assertions.assertThat(sut.getCustomerId()).isEqualTo(payment.getCustomerId());
        Assertions.assertThat(sut.getTotal()).isEqualTo(payment.getTotal());
    }
}
