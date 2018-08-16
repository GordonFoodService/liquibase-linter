package com.wcg.liquibase.config.rules.generic;

import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.resolvers.AddColumnChangeParameterResolver;
import liquibase.change.core.AddColumnChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AddColumnChangeParameterResolver.class)
class PatternRuleTest {


    @DisplayName("Alpha pattern digits string should be invalid")
    @Test
    void alpha_pattern_digits_string_should_be_invalid() {
        PatternRule patternRule = new PatternRule(RuleConfig.builder().withPattern("^[A-Z]+$").build());
        assertTrue(patternRule.invalid("123", null));
    }

    @DisplayName("Alpha pattern alpha string should be valid")
    @Test
    void alpha_pattern_alpha_string_should_be_valid() {
        PatternRule patternRule = new PatternRule(RuleConfig.builder().withPattern("^[A-Z]+$").build());
        assertFalse(patternRule.invalid("ABC", null));
    }

    @DisplayName("Dynamic pattern digits string should be invalid")
    @Test
    void dyanmic_pattern_digits_string_should_be_invalid(AddColumnChange columnChange) {
        PatternRule patternRule = new PatternRule(RuleConfig.builder().withPattern("^TEST-{{value}}$").withDynamicValue("tableName").build());
        columnChange.setTableName("MAGIC");
        assertTrue(patternRule.invalid("123", columnChange));
    }

    @DisplayName("Dynamic pattern value should be valid")
    @Test
    void dyanmic_pattern_digits_vaue_should_be_invalid(AddColumnChange columnChange) {
        PatternRule patternRule = new PatternRule(RuleConfig.builder().withPattern("^TEST-{{value}}$").withDynamicValue("tableName").build());
        columnChange.setTableName("MAGIC");
        assertFalse(patternRule.invalid("TEST-MAGIC", columnChange));
    }

    @DisplayName("Null pattern should be valid")
    @Test
    void null_should_be_valid() {
        PatternRule patternRule = new PatternRule(RuleConfig.builder().build());
        assertFalse(patternRule.invalid("123", null));
    }

}