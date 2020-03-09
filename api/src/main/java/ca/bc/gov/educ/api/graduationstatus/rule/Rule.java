package ca.bc.gov.educ.api.graduationstatus.rule;

import ca.bc.gov.educ.api.graduationstatus.model.dto.ProgramRule;

public interface Rule {

    RuleType ruleType = null;
    
    <T> boolean fire(T parameters);
}
