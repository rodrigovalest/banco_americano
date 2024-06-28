package com.rodrigovalest.ms_calculate.web.controllers;

import com.rodrigovalest.ms_calculate.models.entities.Rule;
import com.rodrigovalest.ms_calculate.services.RuleService;
import com.rodrigovalest.ms_calculate.web.dtos.mapper.RuleMapper;
import com.rodrigovalest.ms_calculate.web.dtos.request.CreateRuleDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateRuleDto createRuleDto) {
        Rule rule = this.ruleService.create(RuleMapper.toEntity(createRuleDto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(rule.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
