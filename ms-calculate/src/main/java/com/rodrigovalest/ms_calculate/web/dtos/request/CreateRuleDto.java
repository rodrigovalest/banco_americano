package com.rodrigovalest.ms_calculate.web.dtos.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRuleDto {

    @NotEmpty(message = "category must not be empty")
    private String category;

    @NotNull(message = "parity must not be null")
    @Min(value = 0, message = "parity must be at least 0")
    private Long parity;
}
