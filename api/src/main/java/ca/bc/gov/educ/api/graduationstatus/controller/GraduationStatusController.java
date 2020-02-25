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

    @Value( "${default.pen}" )
    private String pen;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping (GraduationStatusApiConstants.API_ROOT_MAPPING)
    public Student getResponse() {
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

            achievements.add(ach);
        }

        Student student = new Student();

        student.setPen(pen);
        student.setAchievements(achievements);

        logger.debug("\n\n\n************** PRINTING Details for Student " + pen + " ******************");
        logger.debug(student.toString());
        logger.debug("***********************************************************************************************************\n\n");

        // 2. Remove fails and duplicates
        //=======================================================================================
        List<CourseAchievement> uniqueCourseAchievements = new ArrayList<CourseAchievement>();

        // 2.1 Remove Fails
        uniqueCourseAchievements = GraduationStatusUtils.removeFails(courseAchievements);
        logger.debug("# Fails Removed ");
        logger.debug(uniqueCourseAchievements.toString() + "\n");

        // 2.2 Remove Duplicates
        uniqueCourseAchievements = GraduationStatusUtils.removeDuplicates(uniqueCourseAchievements);
        logger.debug("# Duplicates Removed ");
        logger.debug(uniqueCourseAchievements.toString() + "\n");
        //=======================================================================================

        // 3. Run Grade 12 Credit grade rule
        //=======================================================================================
        MinCreditRule minCreditRule = new MinCreditRule(16);
        boolean hasMinCredits = minCreditRule.execute(uniqueCourseAchievements);

        if (hasMinCredits)
            logger.debug("Min Credit Rule Passed!!!!!!!!!!!!!!!!\n");
        else
            logger.debug("Min Credit Rule Failed XXXXXXXXXXXXXXXXXX\n");
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

        ListIterator<AchievementDto> iter = achievementsCopy.listIterator();

        while(iter.hasNext()) {

            AchievementDto tempAchievement = iter.next();
            ProgramRule tempProgramRule = programRulesMatch.stream()
                    .filter(pr -> tempAchievement
                            .getCourse()
                            .getRequirementCode()
                            .getRequirementCode() == pr.getRequirementCode())
                    .findAny()
                    .orElse(null);

            if(tempProgramRule != null){
                iter.remove();
                logger.debug("Requirement Met -> Requirement Code:" + tempProgramRule.getRequirementCode()
                                + " Course:" + tempAchievement.getCourse().getCourseName() + "\n");

                programRulesMatch.remove(tempProgramRule);
                achievementsCopy.remove(tempAchievement);
            }

        }

        logger.debug("Leftover Course Achievements:" + achievementsCopy + "\n");
        logger.debug("Leftover Program Rules: " + programRulesMatch + "\n");

        //=======================================================================================

        //6. Run elective credit rule
        //=======================================================================================
        minCreditRule = new MinCreditRule(28);
        boolean hasElectiveCredits = minCreditRule.execute(achievementsCopy);

        if (hasElectiveCredits)
            logger.debug("Elective Credit Rule Passed!!!!!!!!!!!!!!!!\n");
        else
            logger.debug("Elective Credit Rule Failed XXXXXXXXXXXXXXXXXX\n");
        //=======================================================================================

        /*if (hasMinCredits && programRulesMatch.size() == 0 && hasElectiveCredits)
            return "Congratulations! " + pen + "is eligible for Graduation. :-)";
        else
            return "Sorry! " + pen + " is not eligible for Graduation. :-(";*/

        return student;
    }

}
