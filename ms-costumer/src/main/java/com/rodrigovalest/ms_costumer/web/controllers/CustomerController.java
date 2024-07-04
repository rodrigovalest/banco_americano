package com.rodrigovalest.ms_costumer.web.controllers;

import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.services.AWSService;
import com.rodrigovalest.ms_costumer.services.CustomerService;
import com.rodrigovalest.ms_costumer.web.dtos.mapper.CustomerMapper;
import com.rodrigovalest.ms_costumer.web.dtos.request.CreateCustomerDto;
import com.rodrigovalest.ms_costumer.web.dtos.request.UpdateCustomerDto;
import com.rodrigovalest.ms_costumer.web.exceptions.RestErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Customer", description = "Feature to create, read, update and delete customers")
@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AWSService awsService;

    @Operation(
            description = "creates a new customer",
            responses = {
                    @ApiResponse(responseCode = "201", description = "CREATED"),
                    @ApiResponse(responseCode = "422", description = "UNPROCESSABLE ENTITY",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateCustomerDto createCustomerDto) {
        Customer newCustomer = this.customerService.create(CustomerMapper.toModel(createCustomerDto), createCustomerDto.getPhoto());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newCustomer.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(
            description = "find a customer by their id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = " application/json;charset=UTF-8")),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long customerId) {
        Customer customer = this.customerService.findById(customerId);
        String base64Photo = this.awsService.download(customer.getUrlPhoto());
        return ResponseEntity.ok(CustomerMapper.toDto(customer, base64Photo));
    }

    @Operation(
            description = "update a customer",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = " application/json;charset=UTF-8")),
                    @ApiResponse(responseCode = "422", description = "UNPROCESSABLE ENTITY",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class)))
            })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long customerId, @Valid @RequestBody UpdateCustomerDto updateCustomerDto) {
        Customer updatedCustomer = this.customerService.update(CustomerMapper.toModel(updateCustomerDto), customerId, updateCustomerDto.getPhoto());
        String base64Photo = this.awsService.download(updatedCustomer.getUrlPhoto());
        return ResponseEntity.ok(CustomerMapper.toDto(updatedCustomer, base64Photo));
    }

    @Operation(
            description = "delete a customer",
            responses = {
                    @ApiResponse(responseCode = "204", description = "NO CONTENT"),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class)))
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long customerId) {
        this.customerService.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }
}
