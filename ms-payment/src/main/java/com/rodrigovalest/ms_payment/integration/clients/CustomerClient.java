package com.rodrigovalest.ms_payment.integration.clients;

import com.rodrigovalest.ms_payment.integration.dtos.response.CustomerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customerClient", url = "${feign.client.customer.url}")
public interface CustomerClient {

    @GetMapping("/v1/customers/{id}")
    CustomerResponseDto getCustomerById(@PathVariable("id") Long customerId);
}
