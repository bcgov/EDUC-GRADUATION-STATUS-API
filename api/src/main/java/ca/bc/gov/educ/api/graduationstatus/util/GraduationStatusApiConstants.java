package ca.bc.gov.educ.api.graduationstatus.util;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class GraduationStatusApiConstants {
    //API end-point Mapping constants
    public static final String API_ROOT_MAPPING = "";
    public static final String API_VERSION = "v1";
    public static final String GRADUATION_STATUS_API_ROOT_MAPPING = "/api/" + API_VERSION + "/graduation-status";
    public static final String GRADUATION_STATUS_BY_PEN = "/{pen}";
    public static final String GRADUATE_STUDENT_BY_PEN = "/{pen}";
    public static final String STUDENT_ACHIEVEMENT_REPORT_BY_PEN = "/{pen}/achievement-report";
    public static final String STUDENT_TRANSCRIPT_BY_PEN = "/{pen}/transcript";

    //Attribute Constants
    public static final String PEN_ATTRIBUTE = "pen";
    public static final String GRAD_STATUS_ID_ATTRIBUTE = "graduationStatusId";

    //Default Attribute value constants
    public static final String DEFAULT_CREATED_BY = "GraduationStatusAPI";
    public static final Date DEFAULT_CREATED_TIMESTAMP = new Date();
    public static final String DEFAULT_UPDATED_BY = "GraduationStatusAPI";
    public static final Date DEFAULT_UPDATED_TIMESTAMP = new Date();

    //Default Date format constants
    public static final String DEFAULT_DATE_FORMAT = "dd-MMM-yyyy";

    //Grade Code Constants
    public static final String FAIL_GRADE_CODE = "F";

    //Rule Type Constants
    public static final String RULE_TYPE_MATCH = "Match";
    public static final String RULE_TYPE_MIN_CREDITS = "MinCredits";
    public static final String RULE_TYPE_MIN_CREDITS_ELECTIVE = "MinCreditsElective";
    public static final String RULE_TYPE_ACTIVE_FLAG_Y = "Y";
    public static final String RULE_TYPE_ACTIVE_FLAG_N = "N";

    //Application Properties Constants
    public static final String ENDPOINT_COURSES_BY_PROGRAM_CODE_URL = "${endpoint.course-api.courses-by-program-code.url}";
    public static final String ENDPOINT_COURSE_ACHIEVEMENTS_BY_PEN_URL = "${endpoint.course-achievement-api.course-achievements-by-pen.url}";
    public static final String ENDPOINT_GET_PROGRAM_RULES_URL = "${endpoint.program-rule.get-program-rules.url}";
    public static final String ENDPOINT_GET_PDF_FROM_HTML_URL = "${endpoint.weasyprint.getPDFfromHTML}";
}
