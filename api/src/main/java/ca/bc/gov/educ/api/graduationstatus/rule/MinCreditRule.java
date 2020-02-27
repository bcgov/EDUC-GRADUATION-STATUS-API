package ca.bc.gov.educ.api.graduationstatus.rule;

import ca.bc.gov.educ.api.graduationstatus.model.dto.AchievementDto;
import ca.bc.gov.educ.api.graduationstatus.model.dto.ProgramRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MinCreditRule {

    private static Logger logger = LoggerFactory.getLogger(MinCreditRule.class);

    public boolean execute(ProgramRule programRule, List<AchievementDto> achievements) {
        int totalCredits = 0;
        int requiredCredits = programRule.getRequiredCredits();

        if (achievements == null || achievements.size() == 0)
            return false;

        if (programRule.getRequiredLevel() == 0) {
            totalCredits = achievements
                    .stream()
                    .filter(achievement -> !achievement.isDuplicate()
                            && !achievement.isFailed()
                    )
                    .mapToInt(achievement -> achievement.getCredits())
                    .sum();
        }
        else {
            totalCredits = achievements
                    .stream()
                    .filter(achievement -> !achievement.isDuplicate()
                            && !achievement.isFailed()
                            && achievement.getCourse().getCourseGradeLevel().startsWith(programRule.getRequiredLevel() + "")
                            )
                    .mapToInt(achievement -> achievement.getCredits())
                    .sum();
        }

        logger.debug("Min Credits -> Required:" + requiredCredits + " Has:" + totalCredits);
        return totalCredits >= requiredCredits;
    }
}
