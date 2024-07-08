package com.rodrigovalest.ms_costumer.integration.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointsQueueMessageDto {
    private Long customerId;
    private Long points;
}

