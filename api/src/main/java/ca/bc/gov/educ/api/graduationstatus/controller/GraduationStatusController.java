package ca.bc.gov.educ.api.graduationstatus.controller;

import ca.bc.gov.educ.api.graduationstatus.model.dto.*;
import ca.bc.gov.educ.api.graduationstatus.rule.MinCreditRule;
import ca.bc.gov.educ.api.graduationstatus.util.GraduationStatusApiConstants;
import ca.bc.gov.educ.api.graduationstatus.util.GraduationStatusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin
@RestController
@RequestMapping (GraduationStatusApiConstants.GRADUATION_STATUS_API_ROOT_MAPPING)
public class GraduationStatusController {

    private static Logger logger = LoggerFactory.getLogger(GraduationStatusController.class);

    @Autowired
    RestTemplate restTemplate;

    @GetMapping (GraduationStatusApiConstants.GRADUATION_STATUS_BY_PEN)
    public Student getResponse(@PathVariable String pen) {

        boolean gradStatusFlag = true;
        logger.debug("#Graduation Status API\n");

        // 1. Get All achievements for a Student
        //=======================================================================================
        CourseAchievement[] courseAchievementsArray = restTemplate.getForObject(
                        "http://localhost:9998/api/v1/course-achievements/pen/" + pen,
                        CourseAchievement[].class);

        List<CourseAchievement> courseAchievements = Arrays.asList(courseAchievementsArray);

        logger.debug("Found " + courseAchievements.size() + " Course Achievements for the Student: " + pen);
        logger.debug(courseAchievements.toString() + "\n");
        //=======================================================================================

        //Populate course achievements with course data

        // TODO: Get Program Code for a Student and use that code to get the courses.
        //      OR send a list of courses from course achievements for a given student
        //         and retrieve the course details from course API
        //          (Needs a new Course API endpoint that supports this)

        //Call course-api for all the courses in 2018 program code
        //------------------------------------------------------------
        Course[] courseArray = restTemplate.getForObject(
                "http://localhost:9999/api/v1/courses?programCode=2018",
                Course[].class);
        List<Course> courses = Arrays.asList(courseArray);

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

        // 2. Remove fails and duplicates
        //=======================================================================================
        List<CourseAchievement> uniqueCourseAchievements = new ArrayList<CourseAchievement>();

        // 2.1 Remove Fails
        int count = courseAchievements.size();
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
        student.setGradMessages(new ArrayList<String>());
        student.setRequirementsMet(new ArrayList<String>());
        student.setRequirementsNotMet(new ArrayList<String>());

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

            if (hasMinCredits) {
                student.getRequirementsMet().add("Met " + currentRule.getRequirementName());
                logger.debug("[" + currentRule.getRequirementName() + "] Rule Passed!!!!!!!!!!!!!!!!\n");
            }
            else {
                gradStatusFlag = false;
                student.getRequirementsNotMet().add("Not met " + currentRule.getNotMetDescription());
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
        List<AchievementDto> achievementsCopy = new ArrayList<AchievementDto>(student.getAchievements());
        List<AchievementDto> finalAchievements = new ArrayList<AchievementDto>();
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

                tempAchievement.setGradRequirementMet(tempProgramRule.getRequirementCode());
                student.getRequirementsMet().add("Met " + tempProgramRule.getRequirementName());
                finalAchievements.add(tempAchievement);
                programRulesMatch.remove(tempProgramRule);
                achievementsCopy.remove(tempAchievement);
            }
        }

        finalAchievements = Stream.concat(finalAchievements.stream()
                , achievementsCopy.stream())
                .collect(Collectors.toList());

        student.setAchievements(finalAchievements);

        logger.debug("Leftover Course Achievements:" + achievementsCopy + "\n");
        logger.debug("Leftover Program Rules: " + programRulesMatch + "\n");

        if (programRulesMatch.size() > 0) {
            gradStatusFlag = false;

            for (ProgramRule programRule : programRulesMatch) {
                student.getRequirementsNotMet().add("Not met " + programRule.getNotMetDescription());
            }

            student.getGradMessages().add("Not all Match rules Met!");
        }
        else {
            student.getGradMessages().add("All Match rules met!");
        }

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
            hasMinCreditsElective = minCreditElectiveRule.execute(currentRule, achievementsCopy);

            if (hasMinCreditsElective) {
                student.getRequirementsMet().add("Met " + currentRule.getRequirementName());
                logger.debug("[" + currentRule.getRequirementName() + "] Rule Passed!!!!!!!!!!!!!!!!\n");
            }
            else {
                gradStatusFlag = false;
                student.getRequirementsNotMet().add("Not met " + currentRule.getNotMetDescription());
                logger.debug("[" + currentRule.getRequirementName() + "] Rule Failed XXXXXXXXXXXXXXXXXX\n");
            }
        }

        if (gradStatusFlag)
            student.getGradMessages().add("Student Graduated!");
        else
            student.getGradMessages().add("Student Not Graduated!");

        return student;
    }

}
