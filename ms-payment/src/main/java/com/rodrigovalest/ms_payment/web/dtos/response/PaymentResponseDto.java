package com.rodrigovalest.ms_payment.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private Long customerId;
    private Long categoryId;
    private Long total;
    private LocalDateTime createdDate;
}
