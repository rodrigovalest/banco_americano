package com.rodrigovalest.ms_costumer.mappers;

import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.models.enums.GenderEnum;
import com.rodrigovalest.ms_costumer.web.dtos.mapper.CustomerMapper;
import com.rodrigovalest.ms_costumer.web.dtos.request.CreateCustomerDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class CustomerMapperTest {

    @Test
    public void fromCreateCustomerDto_WithValidMaleCustomer_ReturnCustomer() {
        CreateCustomerDto createCustomerDto = new CreateCustomerDto(
                "499.130.480-60",
                "Roger",
                "masculino",
                "11/10/1990",
                "roger@email.com",
                "photobase64"
        );

        Customer sut = CustomerMapper.fromDto(createCustomerDto);

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(null);
        Assertions.assertThat(sut.getCpf()).isEqualTo(createCustomerDto.getCpf());
        Assertions.assertThat(sut.getBirthdate()).isEqualTo(LocalDate.of(1990, 10, 11));
        Assertions.assertThat(sut.getName()).isEqualTo(createCustomerDto.getName());
        Assertions.assertThat(sut.getEmail()).isEqualTo(createCustomerDto.getEmail());
        Assertions.assertThat(sut.getGender()).isEqualTo(GenderEnum.MALE);
    }

    @Test
    public void fromCreateCustomerDto_WithValidFemaleCustomer_ReturnCustomer() {
        CreateCustomerDto createCustomerDto = new CreateCustomerDto(
                "499.130.480-60",
                "Maria",
                "feminino",
                "11/10/1990",
                "roger@email.com",
                "photobase64"
        );

        Customer sut = CustomerMapper.fromDto(createCustomerDto);

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(null);
        Assertions.assertThat(sut.getCpf()).isEqualTo(createCustomerDto.getCpf());
        Assertions.assertThat(sut.getBirthdate()).isEqualTo(LocalDate.of(1990, 10, 11));
        Assertions.assertThat(sut.getName()).isEqualTo(createCustomerDto.getName());
        Assertions.assertThat(sut.getEmail()).isEqualTo(createCustomerDto.getEmail());
        Assertions.assertThat(sut.getGender()).isEqualTo(GenderEnum.FEMALE);
    }
}
