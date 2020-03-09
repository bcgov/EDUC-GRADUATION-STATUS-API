package ca.bc.gov.educ.api.graduationstatus.rule;

public enum RuleType {
    MIN_CREDITS ("MinCredits"),
    MATCH ("Match"),
    MIN_CREDITS_ELECTIVE("MinCreditsElective")
    ;

    private final String ruleType;

    RuleType(String ruleType) {
        this.ruleType = ruleType;
    }
}
