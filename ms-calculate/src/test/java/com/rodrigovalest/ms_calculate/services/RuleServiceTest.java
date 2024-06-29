package com.rodrigovalest.ms_calculate.services;

import com.rodrigovalest.ms_calculate.exceptions.EntityNotFoundException;
import com.rodrigovalest.ms_calculate.models.entities.Rule;
import com.rodrigovalest.ms_calculate.repositories.RuleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

    @Test
    public void findById_WithValidId_ReturnsRule() {
        // Arrange
        Long id = 100L;
        Rule rule = new Rule(id, "eletronics", 2L);
        when(this.ruleRepository.findById(id)).thenReturn(Optional.of(rule));

        // Act
        Rule sut = this.ruleService.findById(id);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(id);
        Assertions.assertThat(sut.getParity()).isEqualTo(rule.getParity());
        Assertions.assertThat(sut.getCategory()).isEqualTo(rule.getCategory());
        verify(this.ruleRepository, times(1)).findById(id);
    }

    @Test
    public void findById_WithInexistentId_ThrowsException() {
        Long id = 100L;
        Rule rule = new Rule(id, "eletronics", 2L);
        when(this.ruleRepository.findById(id)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThatThrownBy(() -> this.ruleService.findById(id)).isInstanceOf(EntityNotFoundException.class);

        verify(this.ruleRepository, times(1)).findById(id);
    }

    @Test
    public void update_WithValidData_ReturnsRule() {
        // Arrange
        Long id = 100L;
        Rule toUpdateRule = new Rule(null, "new category", 2L);
        Rule persistedRule = new Rule(id, "old category", 1L);
        Rule updatedRule = new Rule(id, toUpdateRule.getCategory(), toUpdateRule.getParity());
        when(this.ruleRepository.findById(id)).thenReturn(Optional.of(persistedRule));
        when(this.ruleRepository.save(updatedRule)).thenReturn(updatedRule);

        // Act
        Rule sut = this.ruleService.update(toUpdateRule, id);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(id);
        Assertions.assertThat(sut.getParity()).isEqualTo(toUpdateRule.getParity());
        Assertions.assertThat(sut.getCategory()).isEqualTo(toUpdateRule.getCategory());
        verify(this.ruleRepository, times(1)).findById(id);
        verify(this.ruleRepository, times(1)).save(updatedRule);
    }

    @Test
    public void update_WithInexistentId_ThrowsException() {
        Long id = 100L;
        Rule toUpdateRule = new Rule(null, "new category", 2L);
        when(this.ruleRepository.findById(id)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThatThrownBy(() -> this.ruleService.update(toUpdateRule, id)).isInstanceOf(EntityNotFoundException.class);

        verify(this.ruleRepository, times(1)).findById(id);
        verify(this.ruleRepository, times(0)).save(any(Rule.class));
    }
}
