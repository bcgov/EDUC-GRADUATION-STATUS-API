package ca.bc.gov.educ.api.graduationstatus.util;

import java.util.Date;

public class GraduationStatusApiConstants {
    //API end-point Mapping constants
    public static final String API_ROOT_MAPPING = "";
    public static final String API_VERSION = "v1";
    public static final String GRADUATION_STATUS_API_ROOT_MAPPING = "/api/" + API_VERSION + "/graduation-status";
    public static final String GRADUATION_STATUS_BY_PEN = "/{pen}";

    //Attribute Constants
    public static final String PEN_ATTRIBUTE = "pen";

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
}
