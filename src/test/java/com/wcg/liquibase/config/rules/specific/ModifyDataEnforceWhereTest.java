package com.wcg.liquibase.config.rules.specific;

import com.wcg.liquibase.config.RuleConfig;
import liquibase.change.core.AbstractModifyDataChange;
import liquibase.change.core.UpdateDataChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModifyDataEnforceWhereTest {

    private ModifyDataEnforceWhere modifyDataEnforceWhere;

    @BeforeEach
    void setUp() {
        RuleConfig ruleConfig = RuleConfig.builder().withRequireWhere(Collections.singletonList("TESTING_TABLE")).build();
        modifyDataEnforceWhere = new ModifyDataEnforceWhere(ruleConfig);
    }

    @DisplayName("null where clause on table without requirement should be valid")
    @Test
    void null_where_clause_on_table_without_requirement_should_be_valid() {
        assertFalse(modifyDataEnforceWhere.invalid(withWhere("MAGIC", null), null));
    }

    @DisplayName("null where clause on table with requirement should be invalid")
    @Test
    void null_where_clause_on_table_with_requirement_should_be_invalid() {
        assertTrue(modifyDataEnforceWhere.invalid(withWhere("TESTING_TABLE", null), null));
    }

    @DisplayName("null where clause on table with requirement should be invalid")
    @Test
    void where_clause_on_table_with_requirement_should_be_valid() {
        assertFalse(modifyDataEnforceWhere.invalid(withWhere("TESTING_TABLE", "TEST = 'test"), null));
    }

    private AbstractModifyDataChange withWhere(String tableName, String where) {
        UpdateDataChange updateDataChange = new UpdateDataChange();
        updateDataChange.setTableName(tableName);
        updateDataChange.setWhere(where);
        return updateDataChange;

    }
}