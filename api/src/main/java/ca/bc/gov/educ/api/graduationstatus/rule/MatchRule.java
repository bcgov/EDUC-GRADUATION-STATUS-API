package ca.bc.gov.educ.api.graduationstatus.rule;

import ca.bc.gov.educ.api.graduationstatus.model.dto.ProgramRule;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class MatchRule implements Rule {

    @Autowired
    private ProgramRule programRule;

    @Override
    public <T> boolean fire(T parameters) {
        return false;
    }
}
