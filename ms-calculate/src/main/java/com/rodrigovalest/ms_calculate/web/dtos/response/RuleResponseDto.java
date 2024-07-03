package com.rodrigovalest.ms_calculate.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuleResponseDto {
    private String category;
    private Long parity;
}
