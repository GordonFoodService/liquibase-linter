# Liquibase Linter
Liquibase extension to add configurable script linting.

[![Build Status](https://travis-ci.org/whiteclarkegroup/liquibase-linter.svg?branch=master)](https://travis-ci.org/Luke-Rogers/speedcheck)

## Setup
To add `liquibase-linter` into your project just add the artifact as a dependency to the `liquibase-maven-plugin`
The extension will automatically be picked up by liquibase and the rules applied.

```
<plugin>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-maven-plugin</artifactId>
    <configuration>
        <propertyFile>${liquibase.property.file}</propertyFile>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>com.wcg.liquibase</groupId>
            <artifactId>liquibase-linter</artifactId>
            <version>0.3.0</version>
        </dependency>
    </dependencies>
    <executions>
        <execution>
            <id>example</id>
            <phase>generate-resources</phase>
            <goals>
                <goal>${liquibase.goal}</goal>
            </goals>
            <configuration>
                <outputDefaultSchema>true</outputDefaultSchema>
                <changeLogFile>${change.log}</changeLogFile>
                <promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
                <contexts>${active.contexts}</contexts>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## Configuration

To configure `liquibase-linter` add a file named `lqllint.js` to the root of your project. 
This will be picked up by the linter and override any default rules.

### File structure
```
{
  "ignore-context-pattern": "^baseline.*$",
  "rules": {
  
  }
}
```

### Rules
Every rule has basic attributes of:
<ul>
    <li>enabled - Turn rule on and off</li>
    <li>errorMessage - Override default error message for rule</li>
    <li>condition - Spring el expression to conditionally run the rule. Expression takes in the change (I.e. `AddPrimaryKeyChange`) as the data value</li>
</ul>
Specific rules have additional attributes of:
<ul>
    <li>pattern - Regex pattern</li>
    <li>dynamicValue - Spring el expression to resolve the dynamic value from the change to replace {{value}} in the pattern</li>
    <li>maxLength - Max length of a specific value</li>
    <li>requireWhere - Array of values</li>
</ul>

Note: all rules are disabled by default

| Key        | Description           | Additional Attributes |
| ------------- |:-------------:| -----:|
| schema-name      | Pattern that the schema name attribute must follow | <ul><li>pattern</li></ul> |
| file-name-no-spaces      | File name cannot contain any spaces      |  |
| no-preconditions | Preconditions are not allowed to be used      |  |
| has-comment | Change sets must have a comment      |  |
| has-context | Change sets must specify at least one context      |  |
| valid-context | Pattern that the specified contexts must follow      | <ul><li>pattern</li></ul> |
| isolate-ddl-changes | Single ddl change per change set      |  |
| separate-ddl-context | Pattern that the context of the ddl change types must follow      | <ul><li>pattern</li></ul> |
| table-name | Pattern that table name must follow      | <ul><li>pattern</li></ul> |
| table-name-length | Maximum length of the table name      | <ul><li>maxLength</li></ul> |
| object-name | Pattern that any object name must follow      | <ul><li>pattern</li></ul> |
| object-name-length | Maximum length of the object name      | <ul><li>maxLength</li></ul> |
| create-table-remarks | Remarks attribute must be populated      | |
| create-column-remarks | Remarks attribute must be populated      | |
| create-column-nullable-constraint | Create column must specify if the column is nullable      |  |
| create-column-no-define-primary-key | Create column cannot specify primary key, force use of add primary key instead       | |
| modify-data-enforce-where | List of tables that must have a where clause when updating      | <ul><li>requireWhere</li></ul> |
| create-index-name | Pattern index name must follow      | <ul><li>pattern</li><li>dynamicValue</li></ul> |
| unique-constraint-name | Pattern unique constraint name must follow      | <ul><li>pattern</li><li>dynamicValue</li></ul> |
| primary-key-must-be-named | Pattern primary key name must follow | <ul><li>pattern</li></ul> |
| primary-key-must-use-table-name | Primary key must incorporate table name      | <ul><li>pattern</li><li>dynamicValue</li></ul> |
| foreign-key-must-be-named | Pattern foreign key name must follow      | <ul><li>pattern</li></ul> |
| foreign-key-must-use-base-and-referenced-table-name | Foreign key must incorporate base and referenced table name       | <ul><li>pattern</li><li>dynamicValue</li></ul> |

#### Example config file
```
{
  "ignore-context-pattern": "^baseline.*$",
  "rules": {
    "schema-name": {
      "enabled": true,
      "pattern": "^\\$\\{[a-z_]+\\}$",
      "errorMessage": "Must use schema name token, not %s"
    },
    "file-name-no-spaces": {
      "enabled": true,
      "errorMessage": "Changelog filenames should not contain spaces"
    },
    "no-preconditions": {
      "enabled": true,
      "errorMessage": "Preconditions are not allowed in this project"
    },
    "has-comment": {
      "enabled": true,
      "errorMessage": "Change set must have a comment"
    },
    "has-context": {
      "enabled": true,
      "errorMessage": "Should have at least one context on the change set"
    },
    "valid-context": {
      "enabled": true,
      "pattern": "^.*_test|.*_script$",
      "errorMessage": "Context is incorrect, should end with '_test' or '_script'"
    },
    "isolate-ddl-changes": {
      "enabled": true,
      "errorMessage": "Should only have a single ddl change per change set"
    },
    "separate-ddl-context": {
      "enabled": true,
      "pattern": "^.*ddl_test|.*ddl_script$",
      "errorMessage": "Should have a ddl changes under ddl contexts"
    },
    "table-name": {
      "enabled": true,
      "pattern": "^(?!TBL)[A-Z_]+(?<!_)$",
      "errorMessage": "Table '%s' name must be uppercase, use '_' separation and not start with TBL"
    },
    "table-name-length": {
      "enabled": true,
      "maxLength": 26,
      "errorMessage": "Table '%s' name must not be longer than %d"
    },
    "object-name": {
      "enabled": true,
      "pattern": "^(?!_)[A-Z_0-9]+(?<!_)$",
      "errorMessage": "Object name '%s' name must be uppercase and use '_' separation"
    },
    "object-name-length": {
      "enabled": true,
      "maxLength": 30,
      "errorMessage": "Object name '%s' must be less than %d characters"
    },
    "create-table-remarks": {
      "enabled": true,
      "errorMessage": "Create table must contain remark attribute"
    },
    "create-column-remarks": {
      "enabled": true,
      "errorMessage": "Add column must contain remarks"
    },
    "create-column-nullable-constraint": {
      "enabled": true,
      "errorMessage": "Add column must specify nullable constraint"
    },
    "create-column-no-define-primary-key": {
      "enabled": true,
      "errorMessage": "Add column must not use primary key attribute. Instead use AddPrimaryKey change type"
    },
    "modify-data-enforce-where": {
      "enabled": true,
      "errorMessage": "Modify data on table '%s' must have a where condition",
      "requireWhere": [
        "UDT_TABLE_VALUE"
      ]
    },
    "create-index-name": {
      "enabled": true,
      "errorMessage": "Index '%s' must follow pattern table name followed by 'I' and a number e.g. APPLICATION_I1, or match a primary key or unique constraint name",
      "pattern": "^{{value}}_(PK|U\\d|I\\d){1}$",
      "condition": "tableName.length() <= 26",
      "dynamicValue": "tableName"
    },
    "unique-constraint-name": {
      "enabled": true,
      "errorMessage": "Unique constraint '%s' must follow pattern table name followed by 'U' and a number e.g. APPLICATION_U1",
      "pattern": "^{{value}}_U[0-9]+$",
      "condition": "(tableName + '_U').length() <= 28",
      "dynamicValue": "tableName"
    },
    "primary-key-must-be-named": {
      "enabled": true,
      "errorMessage": "Primary key constraint '%s' must end with '_PK' e.g. APPLICATION_PK",
      "pattern": "^[A-Z_]+_PK$"
    },
    "primary-key-must-use-table-name": {
      "enabled": true,
      "errorMessage": "Primary key constraint '%s' must follow pattern table name followed by '_PK' e.g. APPLICATION_PK",
      "pattern": "^{{value}}_PK$",
      "condition": "(tableName + '_PK').length() <= 30",
      "dynamicValue": "tableName"
    },
    "foreign-key-must-be-named": {
      "enabled": true,
      "errorMessage": "Foreign key constraint '%s' must end with '_FK'. e.g. ORDER_CUSTOMER_FK",
      "pattern": "^[A-Z_]+_FK$"
    },
    "foreign-key-must-use-base-and-referenced-table-name": {
      "enabled": true,
      "errorMessage": "Foreign key constraint '%s' must follow pattern {base_table_name}_{parent_table_name}_FK. e.g. ORDER_CUSTOMER_FK",
      "pattern": "^{{value}}_FK$",
      "condition": "(baseTableName + referencedTableName + '_FK').length() <= 30",
      "dynamicValue": "baseTableName + '_' + referencedTableName"
    }
  }
}
```

#### Context ignore pattern
If there is a set of changes that you always want ignored based on their context you can configure this using `ignore-context-pattern`.
