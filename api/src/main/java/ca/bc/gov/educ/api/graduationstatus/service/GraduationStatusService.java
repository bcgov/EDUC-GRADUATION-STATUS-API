package ca.bc.gov.educ.api.graduationstatus.service;

import ca.bc.gov.educ.api.graduationstatus.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.graduationstatus.exception.InvalidParameterException;
import ca.bc.gov.educ.api.graduationstatus.model.dto.*;
import ca.bc.gov.educ.api.graduationstatus.model.entity.GraduationStatusEntity;
import ca.bc.gov.educ.api.graduationstatus.model.entity.StudentAchievementReportEntity;
import ca.bc.gov.educ.api.graduationstatus.model.transformer.GraduationStatusTransformer;
import ca.bc.gov.educ.api.graduationstatus.report.AchievementReport;
import ca.bc.gov.educ.api.graduationstatus.report.StudentReport;
import ca.bc.gov.educ.api.graduationstatus.report.TranscriptReport;
import ca.bc.gov.educ.api.graduationstatus.repository.GraduationStatusRepository;
import ca.bc.gov.educ.api.graduationstatus.repository.StudentReportRepository;
import ca.bc.gov.educ.api.graduationstatus.rule.*;
import ca.bc.gov.educ.api.graduationstatus.model.dto.ProgramRule;
import ca.bc.gov.educ.api.graduationstatus.util.GraduationStatusApiConstants;
import ca.bc.gov.educ.api.graduationstatus.util.GraduationStatusUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GraduationStatusService {

    private static Logger logger = LoggerFactory.getLogger(GraduationStatusService.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private GraduationStatusRepository graduationStatusRepo;

    @Autowired
    StudentReportRepository studentReportRepo;

    @Autowired
    private GraduationStatusTransformer graduationStatusTransformer;

    @Value("${endpoint.course-api.courses-by-program-code.url}")
    private String getCoursesByProgramCodeURL;

    @Value("${endpoint.course-achievement-api.course-achievements-by-pen.url}")
    private String getCourseAchievementsByPenURL;

    @Value("${endpoint.program-rule.get-program-rules.url}")
    private String getProgramRulesURL;

    @Value("${endpoint.weasyprint.getPDFfromHTML}")
    private String getPDFfromHTMLURL;

    public GraduationData graduateStudent(String pen) {
        logger.debug("#Graduation Status API\n");
        boolean gradStatusFlag = true;

        logger.debug("Course Achievements API URL:" +getCourseAchievementsByPenURL);
        logger.debug("Course API URL:" +getCourseAchievementsByPenURL);
        logger.debug("Program Rules API URL:" +getCourseAchievementsByPenURL);

        // 1. Get All achievements for a Student
        //=======================================================================================
        CourseAchievement[] courseAchievementsArray = restTemplate.getForObject(
                getCourseAchievementsByPenURL.replace("{pen}", pen),
                CourseAchievement[].class);

        List<CourseAchievement> courseAchievements = Arrays.asList(courseAchievementsArray);

        logger.debug("Found " + courseAchievements.size() + " Course Achievements for the Student: " + pen);
        logger.debug(courseAchievements.toString() + "\n");
        //=======================================================================================

        //Populate course achievements with course data

        // Get Program Code for a Student and use that code to get the courses.
        //      OR send a list of courses from course achievements for a given student
        //         and retrieve the course details from course API
        //          (Needs a new Course API endpoint that supports this)

        //Call course-api for all the courses in 2018 program code
        //------------------------------------------------------------
        Course[] courseArray = restTemplate.getForObject(getCoursesByProgramCodeURL.replace("{programCode}", "2018"), Course[].class);
        List<Course> courses = Arrays.asList(courseArray);

        //Get all ACTIVE program rules for 2018 program
        //------------------------------------------------------------
        ProgramRule[] programRulesArray = restTemplate.getForObject(getProgramRulesURL, ProgramRule[].class);
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

        RuleEngine ruleEngine = RuleFactory.createRuleEngine();

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

            Rule minCreditsRule = RuleFactory.createRule(RuleType.MIN_CREDITS);
            minCreditsRule = (MinCreditsRule)minCreditsRule;
            ((MinCreditsRule) minCreditsRule).setProgramRule(currentRule);
            hasMinCredits = minCreditsRule.fire(student.getAchievements());

            if (hasMinCredits) {
                student.getRequirementsMet().add("Met " + currentRule.getRequirementName());
                logger.debug("[" + currentRule.getRequirementName() + "] Rule Passed!!!!!!!!!!!!!!!!\n");
            }
            else {
                gradStatusFlag = false;
                student.getRequirementsNotMet().add(currentRule.getNotMetDescription());
                logger.debug("[" + currentRule.getRequirementName() + "] Rule Failed XXXXXXXXXXXXXXXXXX\n");
            }
        }
        //=======================================================================================


        // 4. Read Grad Codes from COURSES
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
                student.getRequirementsNotMet().add(programRule.getNotMetDescription());
            }

            student.getGradMessages().add("Not all Match rules Met!");
        }
        else {
            student.getGradMessages().add("All Match rules met!");
        }

        //=======================================================================================

        //6. Run Min Required Elective credit rule
        //=======================================================================================
        List<ProgramRule> programRulesMinElectiveCredits = programRules
                .stream()
                .filter(programRule ->
                        GraduationStatusApiConstants.RULE_TYPE_MIN_CREDITS_ELECTIVE.compareTo(
                                programRule.getRequirementType()) == 0
                                && GraduationStatusApiConstants.RULE_TYPE_ACTIVE_FLAG_Y.compareToIgnoreCase(
                                programRule.getActiveFlag()) == 0
                )
                .collect(Collectors.toList());

        programRuleIterator = programRulesMinElectiveCredits.listIterator();
        boolean hasMinElectiveCredits = false;

        while(programRuleIterator.hasNext()) {
            ProgramRule currentRule = programRuleIterator.next();

            Rule minElectiveCreditsRule = RuleFactory.createRule(RuleType.MIN_CREDITS_ELECTIVE);
            minElectiveCreditsRule = (MinElectiveCreditsRule)minElectiveCreditsRule;
            ((MinElectiveCreditsRule) minElectiveCreditsRule).setProgramRule(currentRule);
            hasMinElectiveCredits = minElectiveCreditsRule.fire(achievementsCopy);

            if (hasMinElectiveCredits) {
                student.getRequirementsMet().add("Met " + currentRule.getRequirementName());
                logger.debug("[" + currentRule.getRequirementName() + "] Rule Passed!!!!!!!!!!!!!!!!\n");
            }
            else {
                gradStatusFlag = false;
                student.getRequirementsNotMet().add(currentRule.getNotMetDescription());
                logger.debug("[" + currentRule.getRequirementName() + "] Rule Failed XXXXXXXXXXXXXXXXXX\n");
            }
        }

        if (gradStatusFlag)
            student.getGradMessages().add("Student Graduated!");
        else
            student.getGradMessages().add("Student Not Graduated!");

        GraduationData graduationData = new GraduationData();
        graduationData.setPen(pen);
        graduationData.setStatusDate(
                GraduationStatusUtils.formatDate(GraduationStatusApiConstants.DEFAULT_UPDATED_TIMESTAMP)
        );
        try {
            graduationData.getStudentGradData().append(new ObjectMapper().writeValueAsString(student));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            graduationData.getStudentGradData().append("Error reading/writing student data!");
        }

        StudentReport transcriptReport = new TranscriptReport();
        StudentReport achievementReport = new AchievementReport();

        graduationData.setTranscriptReport(transcriptReport.getHtml());
        graduationData.setAchievementReport(achievementReport.getHtml());

        return createGradData(graduationData);
    }

    /**
     * Get Graduation Status data for a Student populated in a GraduationData DTO object
     *
     * @param pen
     * @return GraduationData
     * @throws EntityNotFoundException
     */
    public GraduationData getGraduationData(String pen) {
        GraduationData graduationData = new GraduationData();
        Optional<GraduationStatusEntity> result = graduationStatusRepo.findByPen(pen);

        if (result.isPresent()) {
            graduationData = graduationStatusTransformer.transformToDTO(result.get());
            logger.debug("");
            return graduationData;
        }
        else
            throw new EntityNotFoundException(GraduationStatusEntity.class,
                    GraduationStatusApiConstants.PEN_ATTRIBUTE, pen);
    }

    /**
     * Get Student Achievement Report By PEN
     *
     * @param pen
     * @return GraduationData
     * @throws EntityNotFoundException
     */
    public ResponseEntity<byte[]> getStudentAchievementReportByPen(String pen) {
        String achievementReportInHtml = studentReportRepo.findStudentAchievementReportEntity(pen);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(
                new ByteArrayHttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> entity = new HttpEntity<String>(achievementReportInHtml, headers);

        ResponseEntity<byte[]> achievementReportAsPdf = restTemplate.exchange(
                getPDFfromHTMLURL, HttpMethod.POST, entity, byte[].class, "1");

        return achievementReportAsPdf;
    }

    /**
     * Create a new Graduation Status
     *
     * @param graduationData
     * @return
     * @throws InvalidParameterException
     */
    public GraduationData createGradData(GraduationData graduationData) {

        GraduationStatusEntity gradStatusEntity = new GraduationStatusEntity();

        if(graduationData.getGradStatusId() != null)
            throw new InvalidParameterException(GraduationStatusApiConstants.GRAD_STATUS_ID_ATTRIBUTE);

        gradStatusEntity = graduationStatusTransformer.transformToEntity(graduationData);

        gradStatusEntity.setCreatedBy(GraduationStatusApiConstants.DEFAULT_CREATED_BY);
        gradStatusEntity.setCreatedTimestamp(GraduationStatusApiConstants.DEFAULT_CREATED_TIMESTAMP);
        gradStatusEntity.setUpdatedBy(GraduationStatusApiConstants.DEFAULT_UPDATED_BY);
        gradStatusEntity.setUpdatedTimestamp(GraduationStatusApiConstants.DEFAULT_UPDATED_TIMESTAMP);

        logger.debug("******Graduation Status Entity*****\n" + gradStatusEntity.toString());

        gradStatusEntity = graduationStatusRepo.save(gradStatusEntity);

        return graduationStatusTransformer.transformToDTO(gradStatusEntity);
    }

    /**
     * Update existing Graduation Status
     *
     * @param graduationData
     * @return
     * @throws InvalidParameterException
     */
    public GraduationData updateGradData(GraduationData graduationData) {

        GraduationStatusEntity gradStatusEntity = new GraduationStatusEntity();

        if(graduationData.getGradStatusId() != null)
            throw new InvalidParameterException(GraduationStatusApiConstants.GRAD_STATUS_ID_ATTRIBUTE);

        gradStatusEntity = graduationStatusTransformer.transformToEntity(graduationData);

        gradStatusEntity.setCreatedBy(GraduationStatusApiConstants.DEFAULT_CREATED_BY);
        gradStatusEntity.setCreatedTimestamp(GraduationStatusApiConstants.DEFAULT_CREATED_TIMESTAMP);
        gradStatusEntity.setUpdatedBy(GraduationStatusApiConstants.DEFAULT_UPDATED_BY);
        gradStatusEntity.setUpdatedTimestamp(GraduationStatusApiConstants.DEFAULT_UPDATED_TIMESTAMP);

        logger.debug("******Graduation Status Entity*****\n" + gradStatusEntity.toString());

        gradStatusEntity = graduationStatusRepo.save(gradStatusEntity);

        return graduationStatusTransformer.transformToDTO(gradStatusEntity);
    }

    /**
     * Update a Course
     *
     * @param course
     * @return Course
     * @throws Exception

    public Course updateCourse(Course course, UUID courseId) {

        CourseEntity courseEntity = new CourseEntity();
        Optional<CourseEntity> result = courseRepo.findById(courseId);

        if (result.isPresent()) {
            courseEntity = result.get();
            logger.debug("Before Update:" + courseEntity.toString());

            courseEntity.setCourseName(course.getCourseName());
            courseEntity.setCourseCode(course.getCourseCode());
            courseEntity.setCourseGradeLevel(course.getCourseGradeLevel());
            courseEntity.setCredits(course.getCredits());
            courseEntity.setLanguage(course.getLanguage());
            courseEntity.setCourseStartDate(CourseApiUtils.parseDate(course.getCourseStartDate()));
            courseEntity.setCourseEndDate(CourseApiUtils.parseDate(course.getCourseEndDate()));
            courseEntity.setProgramCode(course.getProgramCode());
            courseEntity.setRequirementCode(course.getRequirementCode());
            courseEntity.setUpdatedBy(CourseApiConstants.DEFAULT_UPDATED_BY);
            courseEntity.setUpdatedTimestamp(new Date());

            courseEntity = courseRepo.save(courseEntity);

            logger.debug("After Update:" + courseEntity.toString());

            return courseTransformer.transformToDTO(courseEntity);
        }
        else
            throw new EntityNotFoundException(CourseEntity.class,
                    CourseApiConstants.COURSE_ID_ATTRIBUTE, course.toString());
    }*/
}
