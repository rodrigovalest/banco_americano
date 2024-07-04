package com.rodrigovalest.ms_costumer.web.controllers;

import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.services.AWSService;
import com.rodrigovalest.ms_costumer.services.CustomerService;
import com.rodrigovalest.ms_costumer.web.dtos.mapper.CustomerMapper;
import com.rodrigovalest.ms_costumer.web.dtos.request.CreateCustomerDto;
import com.rodrigovalest.ms_costumer.web.dtos.request.UpdateCustomerDto;
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

    @Autowired
    private AWSService awsService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateCustomerDto createCustomerDto) {
        Customer newCustomer = this.customerService.create(CustomerMapper.toModel(createCustomerDto), createCustomerDto.getPhoto());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCustomer.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody String photo) {
        String filename = this.awsService.upload(photo);
        return ResponseEntity.ok(filename);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long customerId) {
        Customer customer = this.customerService.findById(customerId);
        return ResponseEntity.ok(CustomerMapper.toDto(customer, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long customerId, @Valid @RequestBody UpdateCustomerDto updateCustomerDto) {
        Customer updatedCustomer = this.customerService.update(CustomerMapper.toModel(updateCustomerDto), customerId);
        return ResponseEntity.ok(CustomerMapper.toDto(updatedCustomer, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long customerId) {
        this.customerService.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }
}
