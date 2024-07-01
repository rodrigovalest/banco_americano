package com.rodrigovalest.ms_costumer.web.controllers;

import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.services.CustomerService;
import com.rodrigovalest.ms_costumer.web.dtos.mapper.CustomerMapper;
import com.rodrigovalest.ms_costumer.web.dtos.request.CreateCustomerDto;
import com.rodrigovalest.ms_costumer.web.dtos.request.UpdateCustomerDto;
import com.rodrigovalest.ms_costumer.web.dtos.response.CustomerResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateCustomerDto createCustomerDto) {
        Customer newCustomer = this.customerService.create(CustomerMapper.toModel(createCustomerDto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCustomer.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long customerId) {
        Customer customer = this.customerService.findById(customerId);
        return ResponseEntity.ok(CustomerMapper.toDto(customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long customerId, @Valid @RequestBody UpdateCustomerDto updateCustomerDto) {
        Customer updatedCustomer = this.customerService.update(CustomerMapper.toModel(updateCustomerDto), customerId);
        return ResponseEntity.ok(CustomerMapper.toDto(updatedCustomer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long customerId) {
        this.customerService.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }
}
