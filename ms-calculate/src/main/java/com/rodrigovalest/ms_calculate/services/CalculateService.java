package com.rodrigovalest.ms_calculate.services;

import com.rodrigovalest.ms_calculate.models.Rule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CalculateService {

    private final RuleService ruleService;

    @Transactional(readOnly = true)
    public Long calculate(Long ruleId, Long value) {
        Rule rule = this.ruleService.findById(ruleId);
        return rule.getParity() * value;
    }
}
