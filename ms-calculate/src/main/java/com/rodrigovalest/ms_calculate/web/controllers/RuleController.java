package com.rodrigovalest.ms_calculate.web.controllers;

import com.rodrigovalest.ms_calculate.models.Rule;
import com.rodrigovalest.ms_calculate.services.RuleService;
import com.rodrigovalest.ms_calculate.web.dtos.mapper.RuleMapper;
import com.rodrigovalest.ms_calculate.web.dtos.request.CreateRuleDto;
import com.rodrigovalest.ms_calculate.web.dtos.request.UpdateRuleDto;
import com.rodrigovalest.ms_calculate.web.exceptions.RestErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Rules", description = "Feature to create, read, update and delete rules")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/rules")
public class RuleController {

    private final RuleService ruleService;

    @Operation(
            description = "creates a new rule",
            responses = {
                    @ApiResponse(responseCode = "201", description = "CREATED"),
                    @ApiResponse(responseCode = "422", description = "UNPROCESSABLE ENTITY",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateRuleDto createRuleDto) {
        Rule rule = this.ruleService.create(RuleMapper.toEntity(createRuleDto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(rule.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(
            description = "find a rule by their id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = " application/json;charset=UTF-8")),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Rule rule = this.ruleService.findById(id);
        return ResponseEntity.ok(RuleMapper.toDto(rule));
    }

    @Operation(
            description = "update a rule",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = " application/json;charset=UTF-8")),
                    @ApiResponse(responseCode = "422", description = "UNPROCESSABLE ENTITY",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class)))
            })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid @RequestBody UpdateRuleDto updateRuleDto) {
        Rule rule = this.ruleService.update(RuleMapper.toEntity(updateRuleDto), id);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri();
        return ResponseEntity.ok(RuleMapper.toDto(rule));
    }

    @Operation(
            description = "delete a rule",
            responses = {
                    @ApiResponse(responseCode = "204", description = "NO CONTENT"),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = RestErrorMessage.class)))
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        this.ruleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
