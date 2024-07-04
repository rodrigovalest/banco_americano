package com.rodrigovalest.ms_costumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigovalest.ms_costumer.exceptions.CpfAlreadyRegisteredException;
import com.rodrigovalest.ms_costumer.exceptions.EmailAlreadyRegistedException;
import com.rodrigovalest.ms_costumer.exceptions.EntityNotFoundException;
import com.rodrigovalest.ms_costumer.exceptions.InvalidCpfException;
import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.models.enums.GenderEnum;
import com.rodrigovalest.ms_costumer.services.AWSService;
import com.rodrigovalest.ms_costumer.services.CustomerService;
import com.rodrigovalest.ms_costumer.web.controllers.CustomerController;
import com.rodrigovalest.ms_costumer.web.dtos.request.CreateCustomerDto;
import com.rodrigovalest.ms_costumer.web.dtos.request.UpdateCustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @MockBean
    private AWSService awsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createCustomer_WithValidData_Return201Created() throws Exception {
        // Arrange
        CreateCustomerDto createCustomerDto = new CreateCustomerDto("499.130.480-60", "Roger", "Masculino", "11/10/1990", "roger@email.com", "photobase64");
        Customer customer = new Customer(0L, "499.130.480-60", "Roger", GenderEnum.MALE, LocalDate.of(1990, 10, 11), "roger@email.com", 0L, "http://somephoto.com");

        when(this.customerService.create(any(Customer.class), anyString())).thenReturn(customer);

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

    @Test
    public void findById_WithValidId_Return200AndCustomer() throws Exception {
        // Arrange
        Long id = 0L;
        Customer customer = new Customer(0L, "499.130.480-60", "Roger", GenderEnum.MALE, LocalDate.of(1990, 10, 11), "roger@email.com", 100L, "photobase64");

        when(this.customerService.findById(id)).thenReturn(customer);

        // Act
        ResultActions response = this.mockMvc.perform(get("/v1/customers/" + id));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value(customer.getCpf()))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.gender").value("masculino"))
                .andExpect(jsonPath("$.birthdate").value("11/10/1990"))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.points").value(customer.getPoints()));
    }

    @Test
    public void findById_WithInexistentId_Throws404NotFound() throws Exception {
        // Arrange
        Long id = 0L;
        when(this.customerService.findById(id)).thenThrow(EntityNotFoundException.class);

        // Act
        ResultActions response = this.mockMvc.perform(get("/v1/customers/" + id));

        // Assert
        response.andExpect(status().isNotFound());
    }

    @Test
    public void update_WithValidData_Returns200AndUpdatedCustomer() throws Exception {
        // Arrange
        Long id = 0L;
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.480-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", "photobase64");
        Customer persistedCustomer = new Customer(id, "499.130.480-60", "old customer", GenderEnum.MALE, LocalDate.of(1990, 10, 11), "oldcustomer@email.com", 100L, "https://oldcustomerimage.com");
        Customer updatedCustomer = new Customer(id, "499.130.480-60", "new customer", GenderEnum.MALE, LocalDate.of(1990, 10, 11), "newcustomer@email.com", 100L, "https://newcustomerimage.com");

        when(this.customerService.findById(id)).thenReturn(persistedCustomer);
        when(this.customerService.update(any(Customer.class), anyLong())).thenReturn(updatedCustomer);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value(updateCustomerDto.getCpf()))
                .andExpect(jsonPath("$.name").value(updateCustomerDto.getName()))
                .andExpect(jsonPath("$.gender").value("masculino"))
                .andExpect(jsonPath("$.birthdate").value("11/10/1990"))
                .andExpect(jsonPath("$.email").value(updateCustomerDto.getEmail()))
                .andExpect(jsonPath("$.points").isNotEmpty());
    }

    @Test
    public void update_WithInexistentId_Throws404NotFoundException() throws Exception {
        // Arrange
        Long id = 0L;
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.480-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", "photobase64");

        when(this.customerService.update(any(Customer.class), anyLong())).thenThrow(EntityNotFoundException.class);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isNotFound());
    }

    @Test
    public void update_WithInvalidCpf_Throws422UnprocessableEntity() throws Exception {
        // Arrange
        Long id = 0L;
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.000-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", "photobase64");

        when(this.customerService.update(any(Customer.class), anyLong())).thenThrow(InvalidCpfException.class);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void update_WithCpfAlreadyRegistered_Throws422UnprocessableEntity() throws Exception {
        // Arrange
        Long id = 0L;
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.000-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", "photobase64");

        when(this.customerService.update(any(Customer.class), anyLong())).thenThrow(CpfAlreadyRegisteredException.class);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void update_WithEmailAlreadyRegistered_Throws422UnprocessableEntity() throws Exception {
        // Arrange
        Long id = 0L;
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.000-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", "photobase64");

        when(this.customerService.update(any(Customer.class), anyLong())).thenThrow(EmailAlreadyRegistedException.class);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void update_WithInvalidData_Throws422UnprocessableEntity() throws Exception {
        // Arrange
        Long id = 0L;
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("49913000060", "", "masculino", "2000/10/01", "newcustomer", "photobase64");

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deleteById_WithValidId_Returns204NoContent() throws Exception {
        // Arrange
        Long id = 1000L;

        // Act
        ResultActions response = this.mockMvc.perform(delete("/v1/customers/" + id));

        // Assert
        response.andExpect(status().isNoContent());
        verify(this.customerService, times(1)).deleteById(id);
    }

    @Test
    public void deleteById_WithInexistentId_Throws404NotFound() throws Exception {
        // Arrange
        Long id = 1000L;
        doThrow(new EntityNotFoundException("Customer with id " + id + " not found"))
                .when(customerService).deleteById(id);

        // Act
        ResultActions response = this.mockMvc.perform(delete("/v1/customers/" + id));

        // Assert
        response.andExpect(status().isNotFound());
        verify(this.customerService, times(1)).deleteById(id);
    }
}
