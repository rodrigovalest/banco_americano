package com.rodrigovalest.ms_payment.integration.dtos.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointsQueuePublisherMessageDto {
    private Long customerId;
    private Long points;
}
