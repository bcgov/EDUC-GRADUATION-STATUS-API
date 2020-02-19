package ca.bc.gov.educ.api.graduationstatus.util;

import java.util.Date;

public class GraduationStatusApiConstants {
    //API end-point Mapping constants
    public static final String API_ROOT_MAPPING = "";
    public static final String API_VERSION = "v1";
    public static final String GRADUATION_STATUS_API_ROOT_MAPPING = "/api/" + API_VERSION + "/graduation-status";

    //Attribute Constants
    public static final String PEN_ATTRIBUTE = "pen";

    //Default Attribute value constants
    public static final String DEFAULT_CREATED_BY = "GraduationStatusAPI";
    public static final Date DEFAULT_CREATED_TIMESTAMP = new Date();
    public static final String DEFAULT_UPDATED_BY = "GraduationStatusAPI";
    public static final Date DEFAULT_UPDATED_TIMESTAMP = new Date();

    //Default Date format constants
    public static final String DEFAULT_DATE_FORMAT = "dd-MMM-yyyy";
}
