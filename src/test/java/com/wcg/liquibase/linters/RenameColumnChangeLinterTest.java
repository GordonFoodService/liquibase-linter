package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.resolvers.ChangeSetParameterResolver;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import liquibase.change.core.RenameColumnChange;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith({ChangeSetParameterResolver.class, DefaultConfigParameterResolver.class})
class RenameColumnChangeLinterTest {

    private RenameColumnChangeLinter renameColumnChangeLinter;
    private ObjectNameLinter objectNameLinter;

    @BeforeEach
    void setUp() {
        objectNameLinter = mock(ObjectNameLinter.class);
        renameColumnChangeLinter = new RenameColumnChangeLinter() {
            public ObjectNameLinter getObjectNameLinter() {
                return objectNameLinter;
            }
        };
    }

    @Test
    void should_use_object_name_linter_for_name_length_check(ChangeSet changeSet, Config config) throws ChangeLogParseException {
        RenameColumnChange renameColumnChange = new RenameColumnChange();
        renameColumnChange.setChangeSet(changeSet);
        renameColumnChange.setNewColumnName("TEST_TEST");
        changeSet.addChange(renameColumnChange);
        renameColumnChangeLinter.lint(renameColumnChange, config.getRules());
        verify(objectNameLinter, times(1)).lintObjectName("TEST_TEST", renameColumnChange, config.getRules());
    }
}