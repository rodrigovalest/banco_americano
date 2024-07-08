package com.rodrigovalest.ms_payment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigovalest.ms_payment.exceptions.CategoryNotFoundException;
import com.rodrigovalest.ms_payment.exceptions.CustomerNotFoundException;
import com.rodrigovalest.ms_payment.exceptions.MicroserviceConnectionErrorException;
import com.rodrigovalest.ms_payment.exceptions.RabbitMqMessagingException;
import com.rodrigovalest.ms_payment.model.Payment;
import com.rodrigovalest.ms_payment.services.PaymentService;
import com.rodrigovalest.ms_payment.web.controllers.PaymentController;
import com.rodrigovalest.ms_payment.web.dtos.request.CreatePaymentRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void create_WithValidData_Returns201Created() throws Exception {
        // Arrange
        CreatePaymentRequestDto createPaymentRequestDto = new CreatePaymentRequestDto(1L, 100L, 10000L);
        Payment payment = new Payment(UUID.randomUUID(), 1L, 100L, 10000L, LocalDateTime.of(2024, 1, 15, 10, 30));

        when(this.paymentService.create(any(Payment.class))).thenReturn(payment);

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPaymentRequestDto)));

        // Assert
        response.andExpect(status().isCreated());
    }

    @Test
    public void create_WithNullData_Returns422UnprocessableEntity() throws Exception {
        // Arrange
        CreatePaymentRequestDto createPaymentRequestDto = new CreatePaymentRequestDto(null, null, null);

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPaymentRequestDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void create_WithInexistentCustomer_Returns404NotFound() throws Exception {
        // Arrange
        CreatePaymentRequestDto createPaymentRequestDto = new CreatePaymentRequestDto(1L, 100L, 10000L);
        when(this.paymentService.create(any(Payment.class))).thenThrow(CustomerNotFoundException.class);

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPaymentRequestDto)));

        // Assert
        response.andExpect(status().isNotFound());
    }

    @Test
    public void create_WithInexistentCategory_Returns404NotFound() throws Exception {
        // Arrange
        CreatePaymentRequestDto createPaymentRequestDto = new CreatePaymentRequestDto(1L, 100L, 10000L);
        when(this.paymentService.create(any(Payment.class))).thenThrow(CategoryNotFoundException.class);

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPaymentRequestDto)));

        // Assert
        response.andExpect(status().isNotFound());
    }

    @Test
    public void create_WithMicroserviceConnectionError_Returns500InternalServerError() throws Exception {
        // Arrange
        CreatePaymentRequestDto createPaymentRequestDto = new CreatePaymentRequestDto(1L, 100L, 10000L);
        when(this.paymentService.create(any(Payment.class))).thenThrow(MicroserviceConnectionErrorException.class);

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPaymentRequestDto)));

        // Assert
        response.andExpect(status().isInternalServerError());
    }

    @Test
    public void create_WithRabbitMqQueueConnectionError_Returns500InternalServerError() throws Exception {
        // Arrange
        CreatePaymentRequestDto createPaymentRequestDto = new CreatePaymentRequestDto(1L, 100L, 10000L);
        when(this.paymentService.create(any(Payment.class))).thenThrow(RabbitMqMessagingException.class);

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPaymentRequestDto)));

        // Assert
        response.andExpect(status().isInternalServerError());
    }

    @Test
    public void findById_WithValidId_Returns200Ok() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        Payment payment = new Payment(id, 1L, 100L, 10000L, LocalDateTime.of(2024, 1, 15, 10, 30, 12));
        when(this.paymentService.findById(id)).thenReturn(payment);

        // Act
        ResultActions response = this.mockMvc.perform(get("/v1/payments/" + id.toString()));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(payment.getCustomerId()))
                .andExpect(jsonPath("$.categoryId").value(payment.getCategoryId()))
                .andExpect(jsonPath("$.total").value(payment.getTotal()))
                .andExpect(jsonPath("$.createdDate").value(payment.getCreatedDate().toString()));
    }

    @Test
    public void findByCustomerId_WithValidCustomerId_Returns200Ok() throws Exception {
        // Arrange
        Long customerId = 23L;
        Payment payment1 = new Payment(UUID.randomUUID(), customerId, 10L, 201L, LocalDateTime.of(2024, Month.JANUARY, 15, 10, 30, 12));
        Payment payment2 = new Payment(UUID.randomUUID(), customerId, 10L, 201L, LocalDateTime.of(2023, Month.FEBRUARY, 11, 9, 45, 2));
        Payment payment3 = new Payment(UUID.randomUUID(), customerId, 10L, 201L, LocalDateTime.of(2022, Month.FEBRUARY, 20, 7, 12, 3));
        Payment payment4 = new Payment(UUID.randomUUID(), customerId, 10L, 201L, LocalDateTime.of(2021, Month.MAY, 25, 23, 59,53));

        when(this.paymentService.findByCustomerId(customerId)).thenReturn(List.of(payment1, payment2, payment3, payment4));

        // Act
        ResultActions response = this.mockMvc.perform(get("/v1/payments/user/" + customerId));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(payment1.getCustomerId()))
                .andExpect(jsonPath("$[0].categoryId").value(payment1.getCategoryId()))
                .andExpect(jsonPath("$[0].total").value(payment1.getTotal()))
                .andExpect(jsonPath("$[0].createdDate").value(payment1.getCreatedDate().toString()))
                .andExpect(jsonPath("$[1].customerId").value(payment2.getCustomerId()))
                .andExpect(jsonPath("$[1].categoryId").value(payment2.getCategoryId()))
                .andExpect(jsonPath("$[1].total").value(payment2.getTotal()))
                .andExpect(jsonPath("$[1].createdDate").value(payment2.getCreatedDate().toString()))
                .andExpect(jsonPath("$[2].customerId").value(payment3.getCustomerId()))
                .andExpect(jsonPath("$[2].categoryId").value(payment3.getCategoryId()))
                .andExpect(jsonPath("$[2].total").value(payment3.getTotal()))
                .andExpect(jsonPath("$[2].createdDate").value(payment3.getCreatedDate().toString()))
                .andExpect(jsonPath("$[3].customerId").value(payment4.getCustomerId()))
                .andExpect(jsonPath("$[3].categoryId").value(payment4.getCategoryId()))
                .andExpect(jsonPath("$[3].total").value(payment4.getTotal()))
                .andExpect(jsonPath("$[3].createdDate").value(payment4.getCreatedDate().toString()));
    }

    @Test
    public void routeNotFound_ShouldThrowResourceNotFound() throws Exception {
        ResultActions response = this.mockMvc.perform(get("/inexistentroute"));
        response.andExpect(status().isNotFound());
    }
}
