package com.rodrigovalest.ms_calculate.repositories;

import com.rodrigovalest.ms_calculate.models.entities.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
}
