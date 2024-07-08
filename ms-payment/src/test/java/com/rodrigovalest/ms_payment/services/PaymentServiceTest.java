package com.rodrigovalest.ms_payment.services;

import com.rodrigovalest.ms_payment.exceptions.*;
import com.rodrigovalest.ms_payment.integration.clients.CalculateClient;
import com.rodrigovalest.ms_payment.integration.clients.CustomerClient;
import com.rodrigovalest.ms_payment.integration.dtos.rabbitmq.PointsQueueMessageDto;
import com.rodrigovalest.ms_payment.integration.dtos.request.CalculateRequestDto;
import com.rodrigovalest.ms_payment.integration.dtos.response.CalculateResponseDto;
import com.rodrigovalest.ms_payment.integration.dtos.response.CustomerResponseDto;
import com.rodrigovalest.ms_payment.integration.rabbitmq.PointsQueuePublisher;
import com.rodrigovalest.ms_payment.model.Payment;
import com.rodrigovalest.ms_payment.repositories.PaymentRepository;
import feign.FeignException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private CalculateClient calculateClient;

    @Mock
    private PointsQueuePublisher pointsQueuePublisher;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    public void create_WithValidData_ReturnsPayment() {
        // Arrange
        Payment toCreatePayment = new Payment(null, 10L, 1L, 1000L, null);
        when(this.customerClient.getCustomerById(toCreatePayment.getCustomerId()))
                .thenReturn(new CustomerResponseDto());
        when(this.calculateClient.calculate(any(CalculateRequestDto.class)))
                .thenReturn(new CalculateResponseDto(1500L));
        when(this.paymentRepository.save(any(Payment.class)))
                .thenReturn(toCreatePayment);

        // Act
        Payment sut = this.paymentService.create(toCreatePayment);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getCategoryId()).isEqualTo(toCreatePayment.getCategoryId());
        Assertions.assertThat(sut.getCustomerId()).isEqualTo(toCreatePayment.getCustomerId());
        Assertions.assertThat(sut.getTotal()).isEqualTo(toCreatePayment.getTotal());

        verify(this.calculateClient, times(1))
                .calculate(any(CalculateRequestDto.class));
        verify(this.customerClient, times(1))
                .getCustomerById(anyLong());
        verify(this.paymentRepository, times(1))
                .save(toCreatePayment);
    }

    @Test
    public void create_WithInexistentCustomer_ThrowsException() {
        Payment toCreatePayment = new Payment(null, 10L, 1L, 1000L, null);
        when(this.customerClient.getCustomerById(toCreatePayment.getCustomerId()))
                .thenThrow(FeignException.NotFound.class);
        Assertions.assertThatThrownBy(() -> this.paymentService.create(toCreatePayment)).isInstanceOf(CustomerNotFoundException.class);

        verify(this.calculateClient, times(0))
                .calculate(any(CalculateRequestDto.class));
        verify(this.customerClient, times(1))
                .getCustomerById(anyLong());
        verify(this.paymentRepository, times(0))
                .save(toCreatePayment);
    }

    @Test
    public void create_WithInexistentCategory_ThrowsException() {
        Payment toCreatePayment = new Payment(null, 10L, 1L, 1000L, null);
        when(this.customerClient.getCustomerById(toCreatePayment.getCustomerId()))
                .thenReturn(new CustomerResponseDto());
        when(this.calculateClient.calculate(any(CalculateRequestDto.class)))
                .thenThrow(FeignException.NotFound.class);

        Assertions.assertThatThrownBy(() -> this.paymentService.create(toCreatePayment)).isInstanceOf(CategoryNotFoundException.class);

        verify(this.calculateClient, times(1))
                .calculate(any(CalculateRequestDto.class));
        verify(this.customerClient, times(1))
                .getCustomerById(anyLong());
        verify(this.paymentRepository, times(0))
                .save(toCreatePayment);
    }

    @Test
    public void create_WithSomeErrorInRabbitMq_ThrowsException() {
        // Arrange
        Payment toCreatePayment = new Payment(null, 10L, 1L, 1000L, null);
        when(this.customerClient.getCustomerById(toCreatePayment.getCustomerId()))
                .thenReturn(new CustomerResponseDto());
        when(this.calculateClient.calculate(any(CalculateRequestDto.class)))
                .thenReturn(new CalculateResponseDto(1500L));
        doThrow(AmqpException.class)
                .when(this.pointsQueuePublisher).sendPointsMessage(any(PointsQueueMessageDto.class));

        // Act & Assert
        Assertions.assertThatThrownBy(() -> this.paymentService.create(toCreatePayment))
                .isInstanceOf(RabbitMqMessagingException.class);

        verify(this.customerClient, times(1))
                .getCustomerById(anyLong());
        verify(this.calculateClient, times(1))
                .calculate(any(CalculateRequestDto.class));
        verify(this.pointsQueuePublisher, times(1))
                .sendPointsMessage(any(PointsQueueMessageDto.class));
        verify(this.paymentRepository, times(0))
                .save(toCreatePayment);
    }

    @Test
    public void create_WithSomeErrorInMsCustomer_ThrowsException() {
        // Arrange
        Payment toCreatePayment = new Payment(null, 10L, 1L, 1000L, null);
        when(this.customerClient.getCustomerById(toCreatePayment.getCustomerId()))
                .thenThrow(FeignException.class);

        // Act & Assert
        Assertions.assertThatThrownBy(() -> this.paymentService.create(toCreatePayment))
                .isInstanceOf(MicroserviceConnectionErrorException.class);

        verify(this.customerClient, times(1))
                .getCustomerById(anyLong());
        verify(this.calculateClient, times(0))
                .calculate(any(CalculateRequestDto.class));
        verify(this.pointsQueuePublisher, times(0))
                .sendPointsMessage(any(PointsQueueMessageDto.class));
        verify(this.paymentRepository, times(0))
                .save(toCreatePayment);
    }

    @Test
    public void create_WithSomeErrorInMsCalculate_ThrowsException() {
        // Arrange
        Payment toCreatePayment = new Payment(null, 10L, 1L, 1000L, null);
        when(this.customerClient.getCustomerById(toCreatePayment.getCustomerId()))
                .thenReturn(new CustomerResponseDto());
        when(this.calculateClient.calculate(any(CalculateRequestDto.class)))
                .thenThrow(FeignException.class);

        // Act & Assert
        Assertions.assertThatThrownBy(() -> this.paymentService.create(toCreatePayment))
                .isInstanceOf(MicroserviceConnectionErrorException.class);

        verify(this.customerClient, times(1))
                .getCustomerById(anyLong());
        verify(this.calculateClient, times(1))
                .calculate(any(CalculateRequestDto.class));
        verify(this.pointsQueuePublisher, times(0))
                .sendPointsMessage(any(PointsQueueMessageDto.class));
        verify(this.paymentRepository, times(0))
                .save(toCreatePayment);
    }

    @Test
    public void findById_WithValidId_ReturnsPayment() {
        // Arrange
        UUID id = UUID.randomUUID();
        Payment payment = new Payment(id, 10L, 1L, 1000L, LocalDateTime.of(2024, 1, 15, 10, 30));
        when(this.paymentRepository.findById(id))
                .thenReturn(Optional.of(payment));

        // Act
        Payment sut = this.paymentService.findById(id);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(id);
        Assertions.assertThat(sut.getCategoryId()).isEqualTo(payment.getCategoryId());
        Assertions.assertThat(sut.getCustomerId()).isEqualTo(payment.getCustomerId());
        Assertions.assertThat(sut.getTotal()).isEqualTo(payment.getTotal());
        Assertions.assertThat(sut.getCreatedDate()).isEqualTo(payment.getCreatedDate());

        verify(this.paymentRepository, times(1))
                .findById(id);
    }

    @Test
    public void findById_WithInexistentId_ThrowsException() {
        UUID id = UUID.randomUUID();
        Payment payment = new Payment(id, 10L, 1L, 1000L, LocalDateTime.of(2024, 1, 15, 10, 30));
        when(this.paymentRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> this.paymentService.findById(id)).isInstanceOf(PaymentNotFoundException.class);

        verify(this.paymentRepository, times(1))
                .findById(id);
    }

    @Test
    public void findByCustomerId_WithValidCustomerId_ReturnsListOfPayments() {
        // Arrange
        Long customerId = 47L;

        when(this.customerClient.getCustomerById(customerId))
                .thenReturn(new CustomerResponseDto());

        Payment payment1 = new Payment(UUID.randomUUID(), customerId, 10L, 201L, LocalDateTime.of(2024, Month.JANUARY, 15, 10, 30));
        Payment payment2 = new Payment(UUID.randomUUID(), customerId, 10L, 201L, LocalDateTime.of(2023, Month.FEBRUARY, 11, 9, 45));
        Payment payment3 = new Payment(UUID.randomUUID(), customerId, 10L, 201L, LocalDateTime.of(2022, Month.FEBRUARY, 20, 7, 12));
        Payment payment4 = new Payment(UUID.randomUUID(), customerId, 10L, 201L, LocalDateTime.of(2021, Month.MAY, 25, 23, 59));

        when(this.paymentRepository.findByCustomerId(customerId))
                .thenReturn(List.of(payment1, payment2, payment3, payment4));

        // Act
        List<Payment> sut = this.paymentService.findByCustomerId(customerId);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.size()).isEqualTo(4);
        Assertions.assertThat(sut).contains(payment1, payment2, payment3, payment4);

        verify(this.customerClient, times(1))
                .getCustomerById(anyLong());
        verify(this.paymentRepository, times(1))
                .findByCustomerId(customerId);
    }

    @Test
    public void findByCustomerId_WithInexistentCustomerId_ThrowsException() {
        Long customerId = 47L;

        when(this.customerClient.getCustomerById(customerId))
                .thenThrow(FeignException.NotFound.class);

        Assertions.assertThatThrownBy(() -> this.paymentService.findByCustomerId(customerId)).isInstanceOf(CustomerNotFoundException.class);

        verify(this.customerClient, times(1))
                .getCustomerById(anyLong());
        verify(this.paymentRepository, times(0))
                .findByCustomerId(customerId);
    }

    @Test
    public void findByCustomerId_WithSomeErrorInCustomerMicroservice_ThrowsException() {
        Long customerId = 47L;

        when(this.customerClient.getCustomerById(customerId))
                .thenThrow(FeignException.class);

        Assertions.assertThatThrownBy(() -> this.paymentService.findByCustomerId(customerId)).isInstanceOf(MicroserviceConnectionErrorException.class);

        verify(this.customerClient, times(1))
                .getCustomerById(anyLong());
        verify(this.paymentRepository, times(0))
                .findByCustomerId(customerId);
    }
}
