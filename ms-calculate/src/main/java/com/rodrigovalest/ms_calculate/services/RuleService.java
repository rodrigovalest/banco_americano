package com.rodrigovalest.ms_calculate.services;

import com.rodrigovalest.ms_calculate.models.entities.Rule;
import com.rodrigovalest.ms_calculate.repositories.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    @Transactional
    public Rule create(Rule rule) {
        return this.ruleRepository.save(rule);
    }
}
