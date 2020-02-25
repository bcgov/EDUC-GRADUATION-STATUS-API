package ca.bc.gov.educ.api.graduationstatus.rule;

import ca.bc.gov.educ.api.graduationstatus.controller.GraduationStatusController;
import ca.bc.gov.educ.api.graduationstatus.model.dto.AchievementDto;
import ca.bc.gov.educ.api.graduationstatus.model.dto.CourseAchievement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MinCreditRule {

    private static Logger logger = LoggerFactory.getLogger(MinCreditRule.class);
    private int minCredits;

    public MinCreditRule(int minCredits) {
        this.minCredits = minCredits;
    }

    public boolean execute(List<?> courseAchievements) {
        int totalCredits = 0;

        if (courseAchievements == null || courseAchievements.size() == 0)
            return false;

        if (courseAchievements.get(0) instanceof CourseAchievement) {
            totalCredits = courseAchievements
                    .stream()
                    .mapToInt(courseAchievement -> ((CourseAchievement)courseAchievement).getCredits())
                    .sum();
        }
        else if (courseAchievements.get(0) instanceof AchievementDto) {
            totalCredits = courseAchievements
                    .stream()
                    .mapToInt(courseAchievement -> ((AchievementDto)courseAchievement).getCredits())
                    .sum();
        }

        logger.debug("Min Credits -> Required:" + minCredits + " Has:" + totalCredits);
        return totalCredits >= minCredits;
    }
}
