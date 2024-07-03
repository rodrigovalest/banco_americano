package com.rodrigovalest.ms_calculate.web.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculateRequestDto {

    @NotNull(message = "value must not be null")
    @Min(value = 0, message = "value must be at least 0")
    private Long value;

    @NotNull(message = "category id must not be null")
    private Long categoryId;
}
