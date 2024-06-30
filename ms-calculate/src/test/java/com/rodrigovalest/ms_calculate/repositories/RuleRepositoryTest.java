package com.rodrigovalest.ms_calculate.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class RuleRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RuleRepository ruleRepository;
}
