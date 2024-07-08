package com.rodrigovalest.ms_costumer.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigovalest.ms_costumer.exceptions.EntityNotFoundException;
import com.rodrigovalest.ms_costumer.exceptions.JsonToDtoConvertException;
import com.rodrigovalest.ms_costumer.integration.dtos.PointsQueueMessageDto;
import com.rodrigovalest.ms_costumer.integration.rabbitmq.PointsQueueConsumer;
import com.rodrigovalest.ms_costumer.services.CustomerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PointsQueueConsumerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private PointsQueueConsumer pointsQueueConsumer;

    @Test
    public void handlePointsMessage_WithValidData_ReturnsVoid() throws JsonProcessingException {
        String message = "{\"customerId\": 1, \"points\": 123}";
        PointsQueueMessageDto dto = new PointsQueueMessageDto(1L, 123L);
        when(this.objectMapper.readValue(message, PointsQueueMessageDto.class)).thenReturn(dto);

        this.pointsQueueConsumer.handlePointsMessage(message);

        verify(this.objectMapper, times(1)).readValue(message, PointsQueueMessageDto.class);
        verify(this.customerService, times(1)).addPointsByCustomerId(dto.getPoints(), dto.getCustomerId());
    }

    @Test
    public void handlePointsMessage_WithInvalidMessage_ThrowsException() throws JsonProcessingException {
        String message = "invalid string dto";
        when(this.objectMapper.readValue(message, PointsQueueMessageDto.class)).thenThrow(JsonProcessingException.class);

        Assertions.assertThatThrownBy(() -> this.pointsQueueConsumer.handlePointsMessage(message))
                .isInstanceOf(JsonToDtoConvertException.class);

        verify(this.objectMapper, times(1)).readValue(message, PointsQueueMessageDto.class);
        verify(this.customerService, times(0)).addPointsByCustomerId(any(), any());
    }

    @Test
    public void handlePointsMessage_WithInexistentId_ThrowsException() throws JsonProcessingException {
        String message = "{\"customerId\": 1, \"points\": 123}";
        PointsQueueMessageDto dto = new PointsQueueMessageDto(1L, 123L);
        when(this.objectMapper.readValue(message, PointsQueueMessageDto.class)).thenReturn(dto);
        doThrow(EntityNotFoundException.class).when(this.customerService).addPointsByCustomerId(dto.getPoints(), dto.getCustomerId());

        Assertions.assertThatThrownBy(() -> this.pointsQueueConsumer.handlePointsMessage(message))
                .isInstanceOf(EntityNotFoundException.class);

        verify(this.objectMapper, times(1)).readValue(message, PointsQueueMessageDto.class);
        verify(this.customerService, times(1)).addPointsByCustomerId(dto.getPoints(), dto.getCustomerId());
    }
}
