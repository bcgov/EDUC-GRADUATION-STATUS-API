package ca.bc.gov.educ.api.graduationstatus.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Component
public class Course {

    private UUID courseId;
    private String courseName;
    private String courseCode;
    private String courseGradeLevel;
    private int credits;
    private String language;
    private String courseStartDate;
    private String courseEndDate;
    private String programCode;
    private int requirementCode;

    @Override
    public String toString() {
        return "\nCourse{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", courseGradeLevel='" + courseGradeLevel + '\'' +
                ", credits=" + credits +
                ", language='" + language + '\'' +
                ", courseStartDate=" + courseStartDate +
                ", courseEndDate=" + courseEndDate +
                ", programCode='" + programCode + '\'' +
                ", requirementCode=" + requirementCode +
                "}";
    }
}
