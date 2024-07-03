package com.rodrigovalest.ms_payment.web.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequestDto {

    @NotNull(message = "customer id must not be null")
    private Long customerId;

    @NotNull(message = "category id must not be null")
    private Long categoryId;

    @NotNull(message = "total must not be null")
    private Long total;
}
