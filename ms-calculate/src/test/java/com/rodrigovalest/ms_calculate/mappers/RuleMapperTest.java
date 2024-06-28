package com.rodrigovalest.ms_calculate.mappers;

import com.rodrigovalest.ms_calculate.models.entities.Rule;
import com.rodrigovalest.ms_calculate.web.dtos.mapper.RuleMapper;
import com.rodrigovalest.ms_calculate.web.dtos.request.CreateRuleDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class RuleMapperTest {

    @Test
    public void toEntityFromCreateRuleDto_WithValidData_ReturnsRule() {
        CreateRuleDto createRuleDto = new CreateRuleDto("eletronics", 10L);

        Rule sut = RuleMapper.toEntity(createRuleDto);

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(null);
        Assertions.assertThat(sut.getCategory()).isEqualTo(createRuleDto.getCategory());
        Assertions.assertThat(sut.getParity()).isEqualTo(createRuleDto.getParity());
    }
}
