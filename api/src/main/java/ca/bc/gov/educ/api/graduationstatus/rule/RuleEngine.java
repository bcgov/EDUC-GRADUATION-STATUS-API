package ca.bc.gov.educ.api.graduationstatus.rule;

import ca.bc.gov.educ.api.graduationstatus.model.dto.ProgramRule;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class RuleEngine<T> {

    private List<Rule> rules;
    private T parameters;

    public RuleEngine() {}

    public RuleEngine(Rule rule) {
        rules.add(rule);
    }

    public RuleEngine(List<Rule> rules) {
        this.setRules(rules);
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void addRules(List<Rule> rules) {
        rules.addAll(rules);
    }

    public boolean fireRules() {
        boolean outcome = true;

        if (rules == null || rules.size() == 0)
            return outcome;
        else {
            for (Rule rule : rules) {

                if (!rule.fire(this.parameters))
                    outcome = false;
            }
        }
        return outcome;
    }
}
