package com.rodrigovalest.ms_calculate.services;

import com.rodrigovalest.ms_calculate.exceptions.EntityNotFoundException;
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

    @Transactional(readOnly = true)
    public Rule findById(Long id) {
        return this.ruleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("rule with id {" + id + "} not found")
        );
    }

    @Transactional
    public Rule update(Rule rule, Long id) {
        Rule persistedRule = this.findById(id);
        persistedRule.setParity(rule.getParity());
        persistedRule.setCategory(rule.getCategory());

        return this.ruleRepository.save(persistedRule);
    }
}
