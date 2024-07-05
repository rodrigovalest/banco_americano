package com.rodrigovalest.ms_calculate.services;

import com.rodrigovalest.ms_calculate.exceptions.EntityNotFoundException;
import com.rodrigovalest.ms_calculate.models.Rule;
import com.rodrigovalest.ms_calculate.repositories.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RuleService {

    private final RuleRepository ruleRepository;

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

    @Transactional
    public void deleteById(Long id) {
        if (!this.ruleRepository.existsById(id))
            throw new EntityNotFoundException("rule with id {" + id + "} not found");
        this.ruleRepository.deleteById(id);
    }
}
