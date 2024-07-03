package com.rodrigovalest.ms_calculate.web.controllers;

import com.rodrigovalest.ms_calculate.services.CalculateService;
import com.rodrigovalest.ms_calculate.web.dtos.request.CalculateRequestDto;
import com.rodrigovalest.ms_calculate.web.dtos.response.CalculateResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/calculate")
public class CalculateController {

    @Autowired
    private CalculateService calculateService;

    @PostMapping
    public ResponseEntity<?> calculate(@Valid @RequestBody CalculateRequestDto calculateRequestDto) {
        Long points = this.calculateService.calculate(calculateRequestDto.getCategoryId(), calculateRequestDto.getValue());
        return ResponseEntity.ok().body(new CalculateResponseDto(points));
    }
}
