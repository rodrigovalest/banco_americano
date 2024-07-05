package com.rodrigovalest.ms_payment.integration.rabbitmq;

import com.rodrigovalest.ms_payment.integration.dtos.rabbitmq.PointsQueueMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointsPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue pointsQueue;

    public void sendPointsMessage(PointsQueueMessageDto message) {
        this.rabbitTemplate.convertAndSend(this.pointsQueue.getName(), message);
    }
}
