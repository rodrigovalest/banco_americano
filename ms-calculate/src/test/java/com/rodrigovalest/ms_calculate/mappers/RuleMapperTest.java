package com.rodrigovalest.ms_calculate.mappers;

import com.rodrigovalest.ms_calculate.models.Rule;
import com.rodrigovalest.ms_calculate.web.dtos.mapper.RuleMapper;
import com.rodrigovalest.ms_calculate.web.dtos.request.CreateRuleDto;
import com.rodrigovalest.ms_calculate.web.dtos.request.UpdateRuleDto;
import com.rodrigovalest.ms_calculate.web.dtos.response.RuleResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class RuleMapperTest {

    @Test
    public void toEntityFromCreateRuleDto_WithValidData_ReturnsRule() {
        // Arrange
        CreateRuleDto createRuleDto = new CreateRuleDto("eletronics", 10L);

        // Act
        Rule sut = RuleMapper.toEntity(createRuleDto);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(null);
        Assertions.assertThat(sut.getCategory()).isEqualTo(createRuleDto.getCategory());
        Assertions.assertThat(sut.getParity()).isEqualTo(createRuleDto.getParity());
    }

    @Test
    public void toDto_WithValidData_ReturnsRuleResposeDto() {
        // Arrange
        Rule rule = new Rule(100L, "eletronics", 10L);

        // Act
        RuleResponseDto sut = RuleMapper.toDto(rule);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getCategory()).isEqualTo(rule.getCategory());
        Assertions.assertThat(sut.getParity()).isEqualTo(rule.getParity());
    }

    @Test
    public void toEntityFromUpdateRuleDto_WithValidData_ReturnsRule() {
        // Arrange
        UpdateRuleDto updateRuleDto = new UpdateRuleDto("eletronics", 10L);

        // Act
        Rule sut = RuleMapper.toEntity(updateRuleDto);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(null);
        Assertions.assertThat(sut.getCategory()).isEqualTo(updateRuleDto.getCategory());
        Assertions.assertThat(sut.getParity()).isEqualTo(updateRuleDto.getParity());
    }

    @Test
    public void ruleMapperConstructor_ShouldInstantiate() {
        RuleMapper sut = new RuleMapper();
        Assertions.assertThat(sut).isNotNull();
    }
}
