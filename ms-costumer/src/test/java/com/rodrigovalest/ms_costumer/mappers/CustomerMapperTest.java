package com.rodrigovalest.ms_costumer.mappers;

import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.models.enums.GenderEnum;
import com.rodrigovalest.ms_costumer.web.dtos.mapper.CustomerMapper;
import com.rodrigovalest.ms_costumer.web.dtos.request.CreateCustomerDto;
import com.rodrigovalest.ms_costumer.web.dtos.request.UpdateCustomerDto;
import com.rodrigovalest.ms_costumer.web.dtos.response.CustomerResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class CustomerMapperTest {

    @Test
    public void toModelFromCreateCustomerDto_WithValidMaleCustomer_ReturnCustomer() {
        CreateCustomerDto createCustomerDto = new CreateCustomerDto(
                "499.130.480-60",
                "Roger",
                "masculino",
                "11/10/1990",
                "roger@email.com",
                "photobase64"
        );

        Customer sut = CustomerMapper.toModel(createCustomerDto);

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(null);
        Assertions.assertThat(sut.getCpf()).isEqualTo(createCustomerDto.getCpf());
        Assertions.assertThat(sut.getBirthdate()).isEqualTo(LocalDate.of(1990, 10, 11));
        Assertions.assertThat(sut.getName()).isEqualTo(createCustomerDto.getName());
        Assertions.assertThat(sut.getEmail()).isEqualTo(createCustomerDto.getEmail());
        Assertions.assertThat(sut.getGender()).isEqualTo(GenderEnum.MALE);
    }

    @Test
    public void toModelFromCreateCustomerDto_WithValidFemaleCustomer_ReturnCustomer() {
        CreateCustomerDto createCustomerDto = new CreateCustomerDto(
                "499.130.480-60",
                "Maria",
                "feminino",
                "11/10/1990",
                "roger@email.com",
                "photobase64"
        );

        Customer sut = CustomerMapper.toModel(createCustomerDto);

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(null);
        Assertions.assertThat(sut.getCpf()).isEqualTo(createCustomerDto.getCpf());
        Assertions.assertThat(sut.getBirthdate()).isEqualTo(LocalDate.of(1990, 10, 11));
        Assertions.assertThat(sut.getName()).isEqualTo(createCustomerDto.getName());
        Assertions.assertThat(sut.getEmail()).isEqualTo(createCustomerDto.getEmail());
        Assertions.assertThat(sut.getGender()).isEqualTo(GenderEnum.FEMALE);
    }

    @Test
    public void toDto_WithValidFemaleCustomer_ReturnsCustomerResponseDto() {
        // Arrange
        Customer customer = new Customer(1L, "499.130.480-60", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L, "http://example.com/photo.jpg");

        // Act
        CustomerResponseDto sut = CustomerMapper.toDto(customer);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(sut.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(sut.getCpf()).isEqualTo(customer.getCpf());
        Assertions.assertThat(sut.getBirthdate()).isEqualTo("01/01/1990");
        Assertions.assertThat(sut.getGender()).isEqualTo("feminino");
        Assertions.assertThat(sut.getPoints()).isEqualTo(customer.getPoints());
    }

    @Test
    public void toDto_WithValidMaleCustomer_ReturnsCustomerResponseDto() {
        // Arrange
        Customer customer = new Customer(1L, "499.130.480-60", "Roger", GenderEnum.MALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L, "http://example.com/photo.jpg");

        // Act
        CustomerResponseDto sut = CustomerMapper.toDto(customer);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(sut.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(sut.getCpf()).isEqualTo(customer.getCpf());
        Assertions.assertThat(sut.getBirthdate()).isEqualTo("01/01/1990");
        Assertions.assertThat(sut.getGender()).isEqualTo("masculino");
        Assertions.assertThat(sut.getPoints()).isEqualTo(customer.getPoints());
    }

    @Test
    public void toModelFromUpdateCustomerDto_WithValidMaleCustomer_ReturnsCustomerResponseDto() {
        // Arrange
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto(
                "499.130.480-60",
                "Roger",
                "masculino",
                "11/10/1990",
                "roger@email.com",
                "photobase64"
        );

        // Act
        Customer sut = CustomerMapper.toModel(updateCustomerDto);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(null);
        Assertions.assertThat(sut.getCpf()).isEqualTo(updateCustomerDto.getCpf());
        Assertions.assertThat(sut.getBirthdate()).isEqualTo(LocalDate.of(1990, 10, 11));
        Assertions.assertThat(sut.getName()).isEqualTo(updateCustomerDto.getName());
        Assertions.assertThat(sut.getEmail()).isEqualTo(updateCustomerDto.getEmail());
        Assertions.assertThat(sut.getGender()).isEqualTo(GenderEnum.MALE);
        Assertions.assertThat(sut.getPoints()).isEqualTo(null);
    }

    @Test
    public void toModelFromUpdateCustomerDto_WithValidFemaleCustomer_ReturnsCustomerResponseDto() {
        // Arrange
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto(
                "499.130.480-60",
                "Geovana",
                "feminino",
                "11/10/1990",
                "geovana@email.com",
                "photobase64"
        );

        // Act
        Customer sut = CustomerMapper.toModel(updateCustomerDto);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(null);
        Assertions.assertThat(sut.getCpf()).isEqualTo(updateCustomerDto.getCpf());
        Assertions.assertThat(sut.getBirthdate()).isEqualTo(LocalDate.of(1990, 10, 11));
        Assertions.assertThat(sut.getName()).isEqualTo(updateCustomerDto.getName());
        Assertions.assertThat(sut.getEmail()).isEqualTo(updateCustomerDto.getEmail());
        Assertions.assertThat(sut.getGender()).isEqualTo(GenderEnum.FEMALE);
        Assertions.assertThat(sut.getPoints()).isEqualTo(null);
    }
}
