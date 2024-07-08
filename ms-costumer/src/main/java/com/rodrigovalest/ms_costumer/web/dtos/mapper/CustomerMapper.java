package com.rodrigovalest.ms_costumer.web.dtos.mapper;

import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.models.enums.GenderEnum;
import com.rodrigovalest.ms_costumer.web.dtos.request.CreateCustomerDto;
import com.rodrigovalest.ms_costumer.web.dtos.request.UpdateCustomerDto;
import com.rodrigovalest.ms_costumer.web.dtos.response.CustomerResponseDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class CustomerMapper {
    public static Customer toModel(CreateCustomerDto createCustomerDto) {
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

    public static CustomerResponseDto toDto(Customer customer, String photo) {
        CustomerResponseDto customerResponseDto = new CustomerResponseDto();
        customerResponseDto.setCpf(customer.getCpf());
        customerResponseDto.setName(customer.getName());
        customerResponseDto.setEmail(customer.getEmail());
        customerResponseDto.setPoints(customer.getPoints());

        if (customer.getGender() == GenderEnum.MALE)
            customerResponseDto.setGender("masculino");
        else if (customer.getGender() == GenderEnum.FEMALE)
            customerResponseDto.setGender("feminino");

        customerResponseDto.setBirthdate(customer.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        customerResponseDto.setPhoto(photo);

        return customerResponseDto;
    }

    public static Customer toModel(UpdateCustomerDto updateCustomerDto) {
        Customer customer = new Customer();

        customer.setId(null);
        customer.setName(updateCustomerDto.getName());
        customer.setCpf(updateCustomerDto.getCpf());
        customer.setEmail(updateCustomerDto.getEmail());
        customer.setPoints(null);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthdate = LocalDate.parse(updateCustomerDto.getBirthdate(), formatter);
        customer.setBirthdate(birthdate);

        if (Objects.equals(updateCustomerDto.getGender().toLowerCase(), "masculino"))
            customer.setGender(GenderEnum.MALE);
        else if (Objects.equals(updateCustomerDto.getGender().toLowerCase(), "feminino"))
            customer.setGender(GenderEnum.FEMALE);

        customer.setUrlPhoto(null);

        return customer;
    }
}
