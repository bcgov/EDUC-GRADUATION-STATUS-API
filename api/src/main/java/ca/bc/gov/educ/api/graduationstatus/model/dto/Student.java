package ca.bc.gov.educ.api.graduationstatus.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Data
@Component
public class Student {
    private String pen;
    private String legalFirstName = "John";
    private String legalMiddleName = "Matthew";
    private String legalLastName = "Doe";
    private String graduationProgram = "2018";
    private String school = "Oak Bay High School";
    private String dob = "01-JAN-2005";
    private String sexCode = "M";
    private String genderCode = "M";
    private String dataSourceCode = "AB12";
    private String usualFirstName = "John";
    private String usualMiddleName = "Matthew";
    private String usualLastName = "Doe";
    private String email = "john.doe@mydomain.ca";
    private String deceasedDate;
    private String gradeCode = "12";
    private String citizenship = "C";
    private String address = "123 Main Street, Victoria BC A1B2C3";
    private List<AchievementDto> achievements;

    @Override
    public String toString() {
        return "\nStudent{" +
                ", pen='" + pen + '\'' +
                ", legalFirstName='" + legalFirstName + '\'' +
                ", legalMiddleName='" + legalMiddleName + '\'' +
                ", legalLastName='" + legalLastName + '\'' +
                ", dob='" + dob + '\'' +
                ", sexCode='" + sexCode + '\'' +
                ", genderCode='" + genderCode + '\'' +
                ", dataSourceCode='" + dataSourceCode + '\'' +
                ", usualFirstName='" + usualFirstName + '\'' +
                ", usualMiddleName='" + usualMiddleName + '\'' +
                ", usualLastName='" + usualLastName + '\'' +
                ", email='" + email + '\'' +
                ", deceasedDate='" + deceasedDate + '\'' +
                ", gradeCode='" + gradeCode + '\'' +
                ", achievements=" + achievements +
                '}';
    }
}
