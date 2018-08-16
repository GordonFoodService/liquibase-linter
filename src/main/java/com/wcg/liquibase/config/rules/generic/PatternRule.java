package com.wcg.liquibase.config.rules.generic;

import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.Rule;
import liquibase.change.Change;

import java.util.regex.Pattern;

public class PatternRule extends Rule {

    private static final String DYNAMIC_VALUE = "{{value}}";

    public PatternRule(RuleConfig ruleConfig) {
        super(ruleConfig);
    }

    private Pattern getDynamicPattern(String value) {
        return Pattern.compile(getRuleConfig().getPatternString().replace(DYNAMIC_VALUE, value));
    }

    private String getDynamicValue(Change change) {
        return getRuleConfig().getDynamicValueExpression()
                .map(expression -> expression.getValue(change, String.class))
                .orElse(null);
    }

    @Override
    public boolean invalid(Object object, Change change) {
        final String value = (String) object;
        if (getRuleConfig().getPatternString() != null) {
            if (getRuleConfig().getPatternString().contains(DYNAMIC_VALUE)) {
                return !getDynamicPattern(getDynamicValue(change)).matcher(value).matches();
            } else {
                return !getRuleConfig().getPattern().map(pattern -> pattern.matcher(value).matches()).orElse(true);
            }
        }
        return false;
    }

    @Override
    protected String buildErrorMessage(Object object, Change change) {
        return String.format(getRuleConfig().getErrorMessage(), object);
    }

}