package com.rodrigovalest.ms_calculate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigovalest.ms_calculate.exceptions.EntityNotFoundException;
import com.rodrigovalest.ms_calculate.services.CalculateService;
import com.rodrigovalest.ms_calculate.web.controllers.CalculateController;
import com.rodrigovalest.ms_calculate.web.dtos.request.CalculateRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculateController.class)
public class CalculateControllerTest {

    @MockBean
    private CalculateService calculateService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void calculate_WithValidData_Returns200Ok() throws Exception {
        // Arrange
        Long points = 2000L;
        CalculateRequestDto calculateRequestDto = new CalculateRequestDto(1000L, 10L);
        when(this.calculateService.calculate(calculateRequestDto.getCategoryId(), calculateRequestDto.getValue()))
                .thenReturn(points);

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(calculateRequestDto)));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(points));

        verify(this.calculateService, times(1))
                .calculate(calculateRequestDto.getCategoryId(), calculateRequestDto.getValue());
    }

    @Test
    public void calculate_WithInexistentCategoryId_Returns404NotFound() throws Exception {
        // Arrange
        Long points = 2000L;
        CalculateRequestDto calculateRequestDto = new CalculateRequestDto(1000L, 10L);
        when(this.calculateService.calculate(calculateRequestDto.getCategoryId(), calculateRequestDto.getValue()))
                .thenThrow(EntityNotFoundException.class);

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(calculateRequestDto)));

        // Assert
        response.andExpect(status().isNotFound());

        verify(this.calculateService, times(1))
                .calculate(calculateRequestDto.getCategoryId(), calculateRequestDto.getValue());
    }
}
