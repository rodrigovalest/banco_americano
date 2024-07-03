package com.rodrigovalest.ms_payment.integration.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculateRequestDto {
    private Long value;
    private Long categoryId;
}
