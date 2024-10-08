package com.rodrigovalest.ms_calculate.web.dtos.mapper;

import com.rodrigovalest.ms_calculate.models.Rule;
import com.rodrigovalest.ms_calculate.web.dtos.request.CreateRuleDto;
import com.rodrigovalest.ms_calculate.web.dtos.request.UpdateRuleDto;
import com.rodrigovalest.ms_calculate.web.dtos.response.RuleResponseDto;

public class RuleMapper {
    public static Rule toEntity(CreateRuleDto createRuleDto) {
        return new Rule(null, createRuleDto.getCategory(), createRuleDto.getParity());
    }

    public static RuleResponseDto toDto(Rule rule){
        return new RuleResponseDto(rule.getCategory(), rule.getParity());
    }

    public static Rule toEntity(UpdateRuleDto updateRuleDto) {
        return new Rule(null, updateRuleDto.getCategory(), updateRuleDto.getParity());
    }
}
