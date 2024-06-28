package com.rodrigovalest.ms_calculate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigovalest.ms_calculate.exceptions.EntityNotFoundException;
import com.rodrigovalest.ms_calculate.models.entities.Rule;
import com.rodrigovalest.ms_calculate.services.RuleService;
import com.rodrigovalest.ms_calculate.web.controllers.RuleController;
import com.rodrigovalest.ms_calculate.web.dtos.request.CreateRuleDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(RuleController.class)
public class RuleControllerTest {

    @MockBean
    private RuleService ruleService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createRule_WithValidData_Returns201Created() throws Exception {
        // Arrange
        CreateRuleDto createRuleDto = new CreateRuleDto("eletronics", 10L);
        Rule rule = new Rule(100L, createRuleDto.getCategory(), createRuleDto.getParity());
        when(this.ruleService.create(any(Rule.class))).thenReturn(rule);

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/rules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(createRuleDto)));

        // Assert
        response.andExpect(status().isCreated());
        verify(this.ruleService, times(1)).create(any(Rule.class));
    }

    @Test
    public void createRule_WithInvalidData_Returns422UnprocessableEntity() throws Exception {
        // Arrange
        CreateRuleDto createRuleDto = new CreateRuleDto(null, -10L);

        // Act
        ResultActions response = this.mockMvc.perform(post("/v1/rules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(createRuleDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());
        verify(this.ruleService, times(0)).create(any(Rule.class));
    }

    @Test
    public void findById_WithValidId_Returns200Ok() throws Exception {
        // Arrange
        Long id = 102L;
        Rule rule = new Rule(id, "eletronics", 2L);
        when(this.ruleService.findById(id)).thenReturn(rule);

        // Act
        ResultActions response = this.mockMvc.perform(get("/v1/rules/" + id));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.parity").value(rule.getParity()))
                .andExpect(jsonPath("$.category").value(rule.getCategory()));
        verify(this.ruleService, times(1)).findById(id);
    }

    @Test
    public void findById_WithInexistentId_Returns404NotFound() throws Exception {
        // Arrange
        Long id = 102L;
        when(this.ruleService.findById(id)).thenThrow(EntityNotFoundException.class);

        // Act
        ResultActions response = this.mockMvc.perform(get("/v1/rules/" + id));

        // Assert
        response.andExpect(status().isNotFound());
        verify(this.ruleService, times(1)).findById(id);
    }
}
