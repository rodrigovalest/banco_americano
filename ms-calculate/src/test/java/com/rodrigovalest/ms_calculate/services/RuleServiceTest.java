package com.rodrigovalest.ms_calculate.services;

import com.rodrigovalest.ms_calculate.models.entities.Rule;
import com.rodrigovalest.ms_calculate.repositories.RuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RuleServiceTest {

    @Mock
    private RuleRepository ruleRepository;

    @InjectMocks
    private RuleService ruleService;

    @Test
    public void createRule_WithValidData_ReturnsRule() {
        // Arrange
        Rule toCreateRule = new Rule(100L, "eletronics", 1L);
        Rule createdRule = new Rule(100L, "eletronics", 1L);
        when(this.ruleRepository.save(toCreateRule)).thenReturn(createdRule);

        // Act
        Rule sut = this.ruleService.create(toCreateRule);

        // Assert
        verify(this.ruleRepository, times(1)).save(toCreateRule);
    }
}
