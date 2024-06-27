package com.rodrigovalest.ms_costumer.web.dtos.mapper;

import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.models.enums.GenderEnum;
import com.rodrigovalest.ms_costumer.web.dtos.request.CreateCustomerDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class CustomerMapper {
    public static Customer fromDto(CreateCustomerDto createCustomerDto) {
        Customer customer = new Customer();

        customer.setId(null);
        customer.setName(createCustomerDto.getName());
        customer.setCpf(createCustomerDto.getCpf());
        customer.setEmail(createCustomerDto.getEmail());
        customer.setPoints(0L);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthdate = LocalDate.parse(createCustomerDto.getBirthdate(), formatter);
        customer.setBirthdate(birthdate);

        if (Objects.equals(createCustomerDto.getGender().toLowerCase(), "masculino"))
            customer.setGender(GenderEnum.MALE);
        else if (Objects.equals(createCustomerDto.getGender().toLowerCase(), "feminino"))
            customer.setGender(GenderEnum.FEMALE);

        customer.setUrlPhoto(null);

        return customer;
    }
}
