package com.rodrigovalest.ms_calculate.web.controllers;

import com.rodrigovalest.ms_calculate.services.CalculateService;
import com.rodrigovalest.ms_calculate.web.dtos.request.CalculateRequestDto;
import com.rodrigovalest.ms_calculate.web.dtos.response.CalculateResponseDto;
import com.rodrigovalest.ms_calculate.web.exceptions.RestErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Calculate", description = "Calculate points given a rule")
@RestController
@RequestMapping("/v1/calculate")
public class CalculateController {

    @Autowired
    private CalculateService calculateService;

    @Operation(
            description = "calculate points",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = " application/json;charset=UTF-8")),
                    @ApiResponse(responseCode = "422", description = "UNPROCESSABLE ENTITY",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<?> calculate(@Valid @RequestBody CalculateRequestDto calculateRequestDto) {
        Long points = this.calculateService.calculate(calculateRequestDto.getCategoryId(), calculateRequestDto.getValue());
        return ResponseEntity.ok().body(new CalculateResponseDto(points));
    }
}
