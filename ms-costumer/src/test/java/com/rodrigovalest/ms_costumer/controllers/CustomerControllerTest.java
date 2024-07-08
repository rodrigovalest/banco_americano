package com.rodrigovalest.ms_costumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigovalest.ms_costumer.exceptions.*;
import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.models.enums.GenderEnum;
import com.rodrigovalest.ms_costumer.services.AWSService;
import com.rodrigovalest.ms_costumer.services.CustomerService;
import com.rodrigovalest.ms_costumer.web.controllers.CustomerController;
import com.rodrigovalest.ms_costumer.web.dtos.mapper.CustomerMapper;
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
    public void createCustomer_WithAWSErrorException_Throws500InternalServerError() throws Exception {
        // Arrange
        CreateCustomerDto createCustomerDto = new CreateCustomerDto("499.130.480-60", "Roger", "Masculino", "11/10/1990", "roger@email.com", "photobase64");
        Customer customer = new Customer(0L, "499.130.480-60", "Roger", GenderEnum.MALE, LocalDate.of(1990, 10, 11), "roger@email.com", 0L, "http://somephoto.com");

        when(this.customerService.create(any(Customer.class), anyString())).thenThrow(AWSErrorException.class);

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerDto)));

        // Assert
        response.andExpect(status().isInternalServerError());
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
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.480-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", base64Photo);
        Customer persistedCustomer = new Customer(id, "499.130.480-60", "old customer", GenderEnum.MALE, LocalDate.of(1990, 10, 11), "oldcustomer@email.com", 100L, "https://oldcustomerimage.com");
        Customer updatedCustomer = new Customer(id, "499.130.480-60", "new customer", GenderEnum.MALE, LocalDate.of(1990, 10, 11), "newcustomer@email.com", 100L, "https://newcustomerimage.com");

        when(this.customerService.findById(id)).thenReturn(persistedCustomer);
        when(this.customerService.update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo)).thenReturn(updatedCustomer);
        when(this.awsService.download(updatedCustomer.getUrlPhoto())).thenReturn(base64Photo);

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
                .andExpect(jsonPath("$.photo").value(base64Photo))
                .andExpect(jsonPath("$.points").isNotEmpty());

        verify(this.customerService, times(1)).update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo);
        verify(this.awsService, times(1)).download(updatedCustomer.getUrlPhoto());
    }

    @Test
    public void update_WithInexistentId_Throws404NotFoundException() throws Exception {
        // Arrange
        Long id = 0L;
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.480-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", base64Photo);

        when(this.customerService.update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo)).thenThrow(EntityNotFoundException.class);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isNotFound());

        verify(this.customerService, times(1)).update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo);
        verify(this.awsService, times(0)).download(anyString());
    }

    @Test
    public void update_WithInvalidCpf_Throws422UnprocessableEntity() throws Exception {
        // Arrange
        Long id = 0L;
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.480-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", base64Photo);

        when(this.customerService.update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo)).thenThrow(InvalidCpfException.class);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());

        verify(this.customerService, times(1)).update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo);
        verify(this.awsService, times(0)).download(anyString());
    }

    @Test
    public void update_WithCpfAlreadyRegistered_Throws422UnprocessableEntity() throws Exception {
        // Arrange
        Long id = 0L;
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.480-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", base64Photo);

        when(this.customerService.update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo)).thenThrow(CpfAlreadyRegisteredException.class);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());

        verify(this.customerService, times(1)).update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo);
        verify(this.awsService, times(0)).download(anyString());
    }

    @Test
    public void update_WithEmailAlreadyRegistered_Throws422UnprocessableEntity() throws Exception {
        // Arrange
        Long id = 0L;
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.480-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", base64Photo);

        when(this.customerService.update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo)).thenThrow(EmailAlreadyRegistedException.class);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());

        verify(this.customerService, times(1)).update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo);
        verify(this.awsService, times(0)).download(anyString());
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

        verify(this.customerService, times(0)).update(any(Customer.class), anyLong(), anyString());
        verify(this.awsService, times(0)).download(anyString());
    }

    @Test
    public void update_WithAWSException_Throws500InternalServerError() throws Exception {
        // Arrange
        Long id = 0L;
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.480-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", base64Photo);
        Customer updatedCustomer = new Customer(id, "499.130.480-60", "new customer", GenderEnum.MALE, LocalDate.of(1990, 10, 11), "newcustomer@email.com", 100L, "https://newcustomerimage.com");

        when(this.customerService.update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo)).thenReturn(updatedCustomer);
        when(this.awsService.download(updatedCustomer.getUrlPhoto())).thenThrow(AWSErrorException.class);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isInternalServerError());

        verify(this.customerService, times(1)).update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo);
        verify(this.awsService, times(1)).download(updatedCustomer.getUrlPhoto());
    }

    @Test
    public void update_WithFileConvertionException_Throws422UnprocessableEntity() throws Exception {
        // Arrange
        Long id = 0L;
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.480-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", base64Photo);
        Customer updatedCustomer = new Customer(id, "499.130.480-60", "new customer", GenderEnum.MALE, LocalDate.of(1990, 10, 11), "newcustomer@email.com", 100L, "https://newcustomerimage.com");

        when(this.customerService.update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo)).thenThrow(FileConvertionException.class);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());

        verify(this.customerService, times(1)).update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo);
        verify(this.awsService, times(0)).download(updatedCustomer.getUrlPhoto());
    }

    @Test
    public void update_WithFileSizeException_Throws413PayloadTooLarge() throws Exception {
        // Arrange
        Long id = 0L;
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("499.130.480-60", "new customer", "masculino", "11/10/1990", "newcustomer@email.com", base64Photo);
        Customer updatedCustomer = new Customer(id, "499.130.480-60", "new customer", GenderEnum.MALE, LocalDate.of(1990, 10, 11), "newcustomer@email.com", 100L, "https://newcustomerimage.com");

        when(this.customerService.update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo)).thenThrow(FileSizeException.class);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerDto)));

        // Assert
        response.andExpect(status().isPayloadTooLarge());

        verify(this.customerService, times(1)).update(CustomerMapper.toModel(updateCustomerDto), id, base64Photo);
        verify(this.awsService, times(0)).download(updatedCustomer.getUrlPhoto());
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

    @Test
    public void routeNotFound_ShouldThrowResourceNotFound() throws Exception {
        ResultActions response = this.mockMvc.perform(get("/inexistentroute"));
        response.andExpect(status().isNotFound());
    }

    @Test
    public void uncoveredExceptionThrowingRuntimeException_Returns500InternalServerError() throws Exception {
        // Arrange
        Long id = 1000L;
        doThrow(new RuntimeException("uncovered error")).when(this.customerService).deleteById(id);

        // Act
        ResultActions response = this.mockMvc.perform(delete("/v1/customers/" + id));

        // Assert
        response.andExpect(status().isInternalServerError());
        verify(this.customerService, times(1)).deleteById(id);
    }
}
