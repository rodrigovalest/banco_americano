package com.rodrigovalest.ms_calculate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigovalest.ms_calculate.exceptions.EntityNotFoundException;
import com.rodrigovalest.ms_calculate.models.Rule;
import com.rodrigovalest.ms_calculate.services.RuleService;
import com.rodrigovalest.ms_calculate.web.controllers.RuleController;
import com.rodrigovalest.ms_calculate.web.dtos.request.CreateRuleDto;
import com.rodrigovalest.ms_calculate.web.dtos.request.UpdateRuleDto;
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

    @Test
    public void update_WithValidData_Returns204NoContent() throws Exception {
        // Arrange
        Long id = 100L;
        UpdateRuleDto updateRuleDto = new UpdateRuleDto("new category", 10L);
        Rule toUpdateRule = new Rule(null, updateRuleDto.getCategory(), updateRuleDto.getParity());
        Rule updatedRule = new Rule(100L, updateRuleDto.getCategory(), updateRuleDto.getParity());
        when(this.ruleService.update(toUpdateRule, id)).thenReturn(updatedRule);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/rules/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updateRuleDto)));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.parity").value(updateRuleDto.getParity()))
                .andExpect(jsonPath("$.category").value(updateRuleDto.getCategory()));
        verify(this.ruleService, times(1)).update(toUpdateRule, id);
    }

    @Test
    public void update_WithInvalidBody_Returns422UnprocessableEntity() throws Exception {
        // Arrange
        Long id = 100L;
        UpdateRuleDto updateRuleDto = new UpdateRuleDto(null, -10L);
        Rule toUpdateRule = new Rule(null, updateRuleDto.getCategory(), updateRuleDto.getParity());

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/rules/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updateRuleDto)));

        // Assert
        response.andExpect(status().isUnprocessableEntity());
        verify(this.ruleService, times(0)).update(any(Rule.class), any(Long.class));
    }

    @Test
    public void update_WithInexistentId_Returns404NotFound() throws Exception {
        // Arrange
        Long id = 100L;
        UpdateRuleDto updateRuleDto = new UpdateRuleDto("new category", 10L);
        Rule toUpdateRule = new Rule(null, updateRuleDto.getCategory(), updateRuleDto.getParity());
        when(this.ruleService.update(toUpdateRule, id)).thenThrow(EntityNotFoundException.class);

        // Act
        ResultActions response = this.mockMvc.perform(put("/v1/rules/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updateRuleDto)));

        // Assert
        response.andExpect(status().isNotFound());
        verify(this.ruleService, times(1)).update(toUpdateRule, id);
    }

    @Test
    public void deleteById_WithValidId_Returns204NoContent() throws Exception {
        // Arrange
        Long id = 100L;

        // Act
        ResultActions response = this.mockMvc.perform(delete("/v1/rules/" + id));

        // Assert
        response.andExpect(status().isNoContent());
        verify(this.ruleService, times(1)).deleteById(id);
    }

    @Test
    public void deleteById_WithInexistentId_Returns404NotFound() throws Exception {
        // Arrange
        Long id = 100L;
        doThrow(new EntityNotFoundException("Customer with id " + id + " not found"))
                .when(this.ruleService).deleteById(id);

        // Act
        ResultActions response = this.mockMvc.perform(delete("/v1/rules/" + id));

        // Assert
        response.andExpect(status().isNotFound());
        verify(this.ruleService, times(1)).deleteById(id);
    }
}
