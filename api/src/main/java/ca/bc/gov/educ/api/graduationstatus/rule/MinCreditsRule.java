package ca.bc.gov.educ.api.graduationstatus.rule;

import ca.bc.gov.educ.api.graduationstatus.model.dto.AchievementDto;
import ca.bc.gov.educ.api.graduationstatus.model.dto.ProgramRule;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
public class MinCreditsRule implements Rule {

    private static Logger logger = LoggerFactory.getLogger(MinCreditsRule.class);

    @Autowired
    private ProgramRule programRule;

    public <T> boolean fire(T parameters) {
        int totalCredits;
        int requiredCredits = programRule.getRequiredCredits();

        List<AchievementDto> achievements = (List<AchievementDto>)parameters;

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
