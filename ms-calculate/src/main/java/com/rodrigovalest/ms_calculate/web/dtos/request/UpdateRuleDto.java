package com.rodrigovalest.ms_calculate.web.dtos.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRuleDto {

    @NotEmpty(message = "category must not be empty")
    private String category;

    @NotNull(message = "parity must not be null")
    @Min(value = 0, message = "parity must be at least 0")
    private Long parity;
}
