package com.rodrigovalest.ms_calculate.web.dtos.mapper;

import com.rodrigovalest.ms_calculate.models.entities.Rule;
import com.rodrigovalest.ms_calculate.web.dtos.request.CreateRuleDto;

public class RuleMapper {
    public static Rule toEntity(CreateRuleDto createRuleDto) {
        return new Rule(null, createRuleDto.getCategory(), createRuleDto.getParity());
    }
}
