package com.rodrigovalest.ms_payment.integration.clients;

import com.rodrigovalest.ms_payment.integration.dtos.CustomerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customerClient", url = "${feign.client.customer.url}")
public interface CustomerClient {

    @GetMapping("/v1/customer/{id}")
    CustomerResponseDto getRuleById(@PathVariable("id") Long id);
}
