package com.rodrigovalest.ms_calculate.services;

import com.rodrigovalest.ms_calculate.models.entities.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CalculateService {

    @Autowired
    private RuleService ruleService;

    @Transactional(readOnly = true)
    public Long calculate(Long ruleId, Long value) {
        Rule rule = this.ruleService.findById(ruleId);
        return rule.getParity() * value;
    }
}
