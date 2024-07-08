package com.rodrigovalest.ms_payment.rabbitmq;

import com.rodrigovalest.ms_payment.integration.dtos.rabbitmq.PointsQueueMessageDto;
import com.rodrigovalest.ms_payment.integration.rabbitmq.PointsQueuePublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PointsQueuePublisherTest {

    @Mock
    private Queue pointsQueue;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PointsQueuePublisher pointsQueuePublisher;

    @Test
    public void sendPointsMessage_WithValidMessage_SendMessageAndReturnsVoid() {
        PointsQueueMessageDto pointsQueueMessageDto = new PointsQueueMessageDto(12L, 1232L);

        this.pointsQueuePublisher.sendPointsMessage(pointsQueueMessageDto);

        verify(this.rabbitTemplate, times(1)).convertAndSend(any(), eq(pointsQueueMessageDto));
    }
}
