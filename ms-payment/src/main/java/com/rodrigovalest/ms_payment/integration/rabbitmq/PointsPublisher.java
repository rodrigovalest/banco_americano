package com.rodrigovalest.ms_payment.integration.rabbitmq;

import com.rodrigovalest.ms_payment.integration.dtos.rabbitmq.PointsQueuePublisherMessageDto;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PointsPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue pointsQueue;

    public void sendPointsMessage(PointsQueuePublisherMessageDto message) {
        this.rabbitTemplate.convertAndSend(this.pointsQueue.getName(), message);
    }
}
