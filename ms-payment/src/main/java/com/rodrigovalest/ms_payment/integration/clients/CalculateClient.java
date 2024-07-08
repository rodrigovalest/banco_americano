package com.rodrigovalest.ms_payment.integration.clients;

import com.rodrigovalest.ms_payment.integration.dtos.request.CalculateRequestDto;
import com.rodrigovalest.ms_payment.integration.dtos.response.CalculateResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "calculateClient", url = "${feign.client.calculate.url}")
public interface CalculateClient {

    @PostMapping("/v1/calculate")
    CalculateResponseDto calculate(@Valid @RequestBody CalculateRequestDto calculateRequestDto);
}
