package ca.bc.gov.educ.api.graduationstatus.controller;

import ca.bc.gov.educ.api.graduationstatus.model.dto.*;
import ca.bc.gov.educ.api.graduationstatus.rule.MinCreditRule;
import ca.bc.gov.educ.api.graduationstatus.util.GraduationStatusApiConstants;
import ca.bc.gov.educ.api.graduationstatus.util.GraduationStatusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping (GraduationStatusApiConstants.GRADUATION_STATUS_API_ROOT_MAPPING)
public class GraduationStatusController {

    private static Logger logger = LoggerFactory.getLogger(GraduationStatusController.class);

    @Autowired
    RestTemplate restTemplate;

    private boolean gradStatusFlag = true;

    @GetMapping (GraduationStatusApiConstants.GRADUATION_STATUS_BY_PEN)
    public Student getResponse(@PathVariable String pen) {
        logger.debug("#Graduation Status API\n");

        // 1. Get All achievements for a Student
        //=======================================================================================
        CourseAchievement[] courseAchievementsArray = restTemplate.getForObject(
                        "http://localhost:9998/api/v1/course-achievements/pen/" + pen,
                        CourseAchievement[].class);

        List<CourseAchievement> courseAchievements = Arrays.asList(courseAchievementsArray);

        logger.debug("# All Course Achievements for the Student: " + pen);
        logger.debug(courseAchievements.toString() + "\n");
        //=======================================================================================

        //Populate course achievements with course data
        //Later, new api for Courses by program code

        //Call course-api for all the courses in 2018 program code
        //------------------------------------------------------------
        Course[] courseArray = restTemplate.getForObject(
                "http://localhost:9999/api/v1/courses?programCode=2018",
                Course[].class);

        List<Course> courses = Arrays.asList(courseArray);

        logger.debug("# All Courses for progran code 2018");
        logger.debug(courses.toString() + "\n");
        //------------------------------------------------------------

        //Get all ACTIVE program rules for 2018 program
        //------------------------------------------------------------
        ProgramRule[] programRulesArray = restTemplate.getForObject(
                "http://localhost:9997/api/v1/program-rules",
                ProgramRule[].class);

        List<ProgramRule> programRules = Arrays.asList(programRulesArray);
        programRules = programRules.stream()
                .filter(p -> GraduationStatusApiConstants.RULE_TYPE_ACTIVE_FLAG_Y
                        .compareToIgnoreCase(p.getActiveFlag()) == 0)
                .collect(Collectors.toList());

        logger.debug("# All Program Rules for 2018 Graduation Program");
        logger.debug(programRules.toString() + "\n");
        //------------------------------------------------------------

        // 2. Remove fails and duplicates
        //=======================================================================================
        List<CourseAchievement> uniqueCourseAchievements = new ArrayList<CourseAchievement>();

        // 2.1 Remove Fails
        courseAchievements = GraduationStatusUtils.markFails(courseAchievements);
        logger.debug("# Fails Removed ");
        logger.debug(courseAchievements.toString() + "\n");

        // 2.2 Remove Duplicates
        courseAchievements = GraduationStatusUtils.markDuplicates(courseAchievements);
        logger.debug("# Duplicates Removed ");
        logger.debug(courseAchievements.toString() + "\n");
        //=======================================================================================

        List<AchievementDto> achievements = new ArrayList<AchievementDto>();

        for (CourseAchievement ca : courseAchievements) {
            //Course DTO
            CourseDto cDto = new CourseDto();
            Course course = courses.stream()
                    .filter(c -> ca.getCourseId().equals(c.getCourseId()))
                    .findAny()
                    .orElse(null);

            if (course != null) {

                //Program Code DTO
                ProgramCodeDto pcDto = new ProgramCodeDto();
                pcDto.setProgramCode("2018");
                pcDto.setProgramName("2018 Graduation Program");
                pcDto.setProgramStartDate("01-SEP-2017");

                //Program Rule DTO
                ProgramRuleDto prDto = new ProgramRuleDto();
                ProgramRule programRule = programRules.stream()
                        .filter(p -> course.getRequirementCode() == p.getRequirementCode())
                        .findAny()
                        .orElse(null);

                if (programRule != null) {
                    prDto.setRequirementCode(programRule.getRequirementCode());
                    prDto.setRequirementName(programRule.getRequirementName());
                    prDto.setRequiredCredits(programRule.getRequiredCredits());
                    prDto.setNotMetDescription(programRule.getNotMetDescription());
                    prDto.setProgramCode(pcDto);
                    prDto.setRequirementType(programRule.getRequirementType());
                }

                cDto.setCourseId(course.getCourseId());
                cDto.setCourseName(course.getCourseName());
                cDto.setCourseCode(course.getCourseCode());
                cDto.setCourseGradeLevel(course.getCourseGradeLevel());
                cDto.setCredits(course.getCredits());
                cDto.setLanguage(course.getLanguage());
                cDto.setCourseStartDate(course.getCourseStartDate());
                cDto.setCourseEndDate(course.getCourseEndDate());
                cDto.setProgramCode(pcDto);
                cDto.setRequirementCode(prDto);

            }

            AchievementDto ach = new AchievementDto();
            ach.setCourseAchievementId(ca.getCourseAchievementId());
            ach.setSessionDate(ca.getSessionDate());
            ach.setFinalPercent(ca.getFinalPercent());
            ach.setInterimPercent(ca.getInterimPercent());
            ach.setFinalLetterGrade(ca.getFinalLetterGrade());
            ach.setCredits(ca.getCredits());
            ach.setCourse(cDto);
            ach.setCourseType(ca.getCourseType());
            ach.setInterimLetterGrade(ca.getInterimLetterGrade());
            ach.setPen(ca.getPen());
            ach.setFailed(ca.isFailed());
            ach.setDuplicate(ca.isDuplicate());

            achievements.add(ach);
        }

        Student student = new Student();

        student.setPen(pen);
        student.setAchievements(achievements);

        logger.debug("\n\n\n************** PRINTING Details for Student " + pen + " ******************");
        logger.debug(student.toString());
        logger.debug("***********************************************************************************************************\n\n");

        // 3. Run Min Required Credit rules
        //=======================================================================================
        List<ProgramRule> programRulesMinCredits = programRules
                .stream()
                .filter(programRule ->
                        GraduationStatusApiConstants.RULE_TYPE_MIN_CREDITS.compareTo(
                                programRule.getRequirementType()) == 0
                        && GraduationStatusApiConstants.RULE_TYPE_ACTIVE_FLAG_Y.compareToIgnoreCase(
                                programRule.getActiveFlag()) == 0
                        )
                .collect(Collectors.toList());

        ListIterator<ProgramRule> programRuleIterator = programRulesMinCredits.listIterator();
        boolean hasMinCredits = false;

        while(programRuleIterator.hasNext()) {
            ProgramRule currentRule = programRuleIterator.next();

            MinCreditRule minCreditRule = new MinCreditRule();
            hasMinCredits = minCreditRule.execute(currentRule, student.getAchievements());

            if (hasMinCredits)
                logger.debug("[" + currentRule.getRequirementName() + "] Rule Passed!!!!!!!!!!!!!!!!\n");
            else {
                gradStatusFlag = false;
                logger.debug("[" + currentRule.getRequirementName() + "] Rule Failed XXXXXXXXXXXXXXXXXX\n");
            }
        }
        //=======================================================================================


        //TODO: 4. Read Grad Codes from COURSES
        //=======================================================================================
        // Achievements assembled above
        //=======================================================================================

        //5. Run course specific grad rules
        //=======================================================================================
        List<AchievementDto> achievementsCopy = new ArrayList<>(achievements);
        List<ProgramRule> programRulesMatch = programRules
                .stream()
                .filter(programRule ->
                        GraduationStatusApiConstants.RULE_TYPE_MATCH.compareTo(
                                programRule.getRequirementType()) == 0)
                .collect(Collectors.toList());

        ListIterator<AchievementDto> achievementsIterator = achievementsCopy.listIterator();

        while(achievementsIterator.hasNext()) {

            AchievementDto tempAchievement = achievementsIterator.next();
            ProgramRule tempProgramRule = programRulesMatch.stream()
                    .filter(pr -> tempAchievement
                            .getCourse()
                            .getRequirementCode()
                            .getRequirementCode() == pr.getRequirementCode())
                    .findAny()
                    .orElse(null);

            if(tempProgramRule != null){
                achievementsIterator.remove();
                logger.debug("Requirement Met -> Requirement Code:" + tempProgramRule.getRequirementCode()
                                + " Course:" + tempAchievement.getCourse().getCourseName() + "\n");

                programRulesMatch.remove(tempProgramRule);
                achievementsCopy.remove(tempAchievement);
            }
        }

        logger.debug("Leftover Course Achievements:" + achievementsCopy + "\n");
        logger.debug("Leftover Program Rules: " + programRulesMatch + "\n");

        //=======================================================================================

        //6. Run Min Required Elective credit rule
        //=======================================================================================
        List<ProgramRule> programRulesMinCreditsElectives = programRules
                .stream()
                .filter(programRule ->
                        GraduationStatusApiConstants.RULE_TYPE_MIN_CREDITS_ELECTIVE.compareTo(
                                programRule.getRequirementType()) == 0
                                && GraduationStatusApiConstants.RULE_TYPE_ACTIVE_FLAG_Y.compareToIgnoreCase(
                                programRule.getActiveFlag()) == 0
                )
                .collect(Collectors.toList());

        programRuleIterator = programRulesMinCreditsElectives.listIterator();
        boolean hasMinCreditsElective = false;

        while(programRuleIterator.hasNext()) {
            ProgramRule currentRule = programRuleIterator.next();

            MinCreditRule minCreditElectiveRule = new MinCreditRule();
            hasMinCreditsElective = minCreditElectiveRule.execute(currentRule, student.getAchievements());

            if (hasMinCreditsElective)
                logger.debug("[" + currentRule.getRequirementName() + "] Rule Passed!!!!!!!!!!!!!!!!\n");
            else {
                gradStatusFlag = false;
                logger.debug("[" + currentRule.getRequirementName() + "] Rule Failed XXXXXXXXXXXXXXXXXX\n");
            }
        }

        return student;
    }

}
