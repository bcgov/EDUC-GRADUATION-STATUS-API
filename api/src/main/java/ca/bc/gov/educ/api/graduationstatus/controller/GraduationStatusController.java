package ca.bc.gov.educ.api.graduationstatus.controller;

import ca.bc.gov.educ.api.graduationstatus.model.dto.CourseAchievement;
import ca.bc.gov.educ.api.graduationstatus.util.GraduationStatusApiConstants;
import ca.bc.gov.educ.api.graduationstatus.util.GraduationStatusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping (GraduationStatusApiConstants.GRADUATION_STATUS_API_ROOT_MAPPING)
public class GraduationStatusController {

    private static Logger logger = LoggerFactory.getLogger(GraduationStatusController.class);

    @Autowired
    RestTemplate restTemplate;

    @GetMapping (GraduationStatusApiConstants.API_ROOT_MAPPING)
    public List<CourseAchievement> getResponse() {
        logger.debug("#Graduation Status API");

        // 1. Get All achievements for a Student
        CourseAchievement[] courseAchievementsArray =
                restTemplate.getForObject(
                        "http://localhost:9998/api/v1/course-achievements/pen/123383473",
                        CourseAchievement[].class);

        List<CourseAchievement> courseAchievements = Arrays.asList(courseAchievementsArray);

        logger.debug("# All Course Achievements for the Student: 123383473");
        logger.debug(courseAchievements.toString());

        // 2. Remove fails and duplicates
        //====================================
        List<CourseAchievement> uniqueCourseAchievements = new ArrayList<CourseAchievement>();

        // 2.1 Remove Fails
        uniqueCourseAchievements = GraduationStatusUtils.removeFails(courseAchievements);
        logger.debug("\n# Fails Removed ");
        logger.debug(uniqueCourseAchievements.toString());

        // 2.2 Remove Duplicates
        uniqueCourseAchievements = GraduationStatusUtils.removeDuplicates(uniqueCourseAchievements);
        logger.debug("\n# Duplicates Removed ");
        logger.debug(uniqueCourseAchievements.toString());

        //TODO: 3. Read Grad Codes from COURSES

        //TODO: 4. Run Grade 12 Credit grade rule

        //TODO: 5. Run course specific grad rules

        //TODO: 6. Run elective credit rule

        return uniqueCourseAchievements;
    }

}
