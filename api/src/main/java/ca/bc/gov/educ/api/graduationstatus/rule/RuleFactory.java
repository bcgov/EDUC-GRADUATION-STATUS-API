package ca.bc.gov.educ.api.graduationstatus.rule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleFactory {

    @Autowired
    private static RuleEngine ruleEngine;

    public static Rule createRule(RuleType ruleType) {
        switch (ruleType) {
            case MIN_CREDITS:
                return new MinCreditsRule();
            case MATCH:
                return new MatchRule();
            case MIN_CREDITS_ELECTIVE:
                return new MinElectiveCreditsRule();
            default:
                return new MatchRule();
        }
    }

    public static RuleEngine createRuleEngine() {
        return ruleEngine;
    }

    public static RuleEngine createRuleEngine(Rule rule) {
        ruleEngine.addRule(rule);
        return ruleEngine;
    }

    public static RuleEngine createRuleEngine(List<Rule> rules) {
        ruleEngine.addRules(rules);
        return ruleEngine;
    }
}
