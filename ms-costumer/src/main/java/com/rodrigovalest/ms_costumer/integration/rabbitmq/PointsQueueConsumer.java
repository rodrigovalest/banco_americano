package com.rodrigovalest.ms_costumer.integration.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigovalest.ms_costumer.exceptions.JsonToDtoConvertException;
import com.rodrigovalest.ms_costumer.integration.dtos.PointsQueueMessageDto;
import com.rodrigovalest.ms_costumer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class PointsQueueConsumer {

    private final ObjectMapper objectMapper;
    private final CustomerService customerService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handlePointsMessage(String message) {
        try {
            PointsQueueMessageDto dto = this.objectMapper.readValue(message, PointsQueueMessageDto.class);
            this.customerService.addPointsByCustomerId(dto.getPoints(), dto.getCustomerId());
        } catch (JsonProcessingException e) {
            log.info("ERROR trying to convert string to PointsQueueMessageDto: {}", e.getMessage());
            throw new JsonToDtoConvertException("ERROR trying to convert message to PointsQueueMessageDto: " + e.getMessage());
        }
    }
}
