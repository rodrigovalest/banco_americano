package com.rodrigovalest.ms_calculate.services;

import com.rodrigovalest.ms_calculate.exceptions.EntityNotFoundException;
import com.rodrigovalest.ms_calculate.models.Rule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalculateServiceTest {

    @Mock
    private RuleService ruleService;

    @InjectMocks
    private CalculateService calculateService;

    @Test
    public void calculate_WithValidData_ReturnsPoints() {
        // Arrange
        Long id = 100L;
        Long value = 1000L;
        Rule rule = new Rule(id, "eletronics", 3L);
        when(this.ruleService.findById(id)).thenReturn(rule);

        // Act
        Long sut = this.calculateService.calculate(id, value);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut).isEqualTo(3000L);
        verify(this.ruleService, times(1)).findById(id);
    }

    @Test
    public void calculate_WithInexistentId_ThrowsException() {
        Long id = 100L;
        Long value = 1000L;
        when(this.ruleService.findById(id)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThatThrownBy(() -> this.calculateService.calculate(id, value)).isInstanceOf(EntityNotFoundException.class);

        verify(this.ruleService, times(1)).findById(id);
    }
}
