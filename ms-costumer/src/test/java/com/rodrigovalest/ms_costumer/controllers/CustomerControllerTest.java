package com.rodrigovalest.ms_costumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.models.enums.GenderEnum;
import com.rodrigovalest.ms_costumer.services.CustomerService;
import com.rodrigovalest.ms_costumer.web.controllers.CustomerController;
import com.rodrigovalest.ms_costumer.web.dtos.request.CreateCustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createCustomer_WithValidData_Return201Created() throws Exception {
        // Arrange
        CreateCustomerDto createCustomerDto = new CreateCustomerDto("499.130.480-60", "Roger", "Masculino", "11/10/1990", "roger@email.com", "photobase64");
        Customer customer = new Customer(0L, "499.130.480-60", "Roger", GenderEnum.MALE, LocalDate.of(1990, 10, 11), "roger@email.com", 0L, "photobase64");

        when(this.customerService.create(any(Customer.class))).thenReturn(customer);

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerDto)));

        // Assert
        response.andExpect(status().isCreated());
    }

    @Test
    public void createCustomer_WithInvalidCpf_Return422UnprocessableEntity() throws Exception {
        // Arrange
        CreateCustomerDto createCustomerDto = new CreateCustomerDto("INVALID CPF", "Roger", "Masculino", "11/10/1990", "roger@email.com", "photobase64");

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createCustomer_WithInvalidEmail_Return422UnprocessableEntity() throws Exception {
        // Arrange
        CreateCustomerDto createCustomerDto = new CreateCustomerDto("499-130-480.60", "Roger", "Masculino", "11/10/1990", "INVALID EMAIL", "photobase64");

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createCustomer_WithInvalidName_Return422UnprocessableEntity() throws Exception {
        // Arrange
        CreateCustomerDto createCustomerDto = new CreateCustomerDto("499-130-480.60", "A", "Masculino", "11/10/1990", "roger@email.com", "photobase64");

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createCustomer_WithInvalidBirthdate_Return422UnprocessableEntity() throws Exception {
        // Arrange
        CreateCustomerDto createCustomerDto = new CreateCustomerDto("499-130-480.60", "Roger", "Masculino", "1990/11/10", "roger@email.com", "photobase64");

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createCustomer_WithInvalidDto_Return422UnprocessableEntity() throws Exception {
        // Arrange
        CreateCustomerDto createCustomerDto = new CreateCustomerDto();

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());
    }
}
