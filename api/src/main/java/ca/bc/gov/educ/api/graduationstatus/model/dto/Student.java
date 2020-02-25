package ca.bc.gov.educ.api.graduationstatus.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Data
@Component
public class Student {
    private UUID studentId;
    private String pen;
    private String legalFirstName;
    private String legalMiddleName;
    private String legalLastName;
    private String dob;
    private String sexCode;
    private String genderCode;
    private String dataSourceCode;
    private String usualFirstName;
    private String usualMiddleName;
    private String usualLastName;
    private String email;
    private String deceasedDate;
    private String gradeCode;
    private List<AchievementDto> achievements;

    @Override
    public String toString() {
        return "\nStudent{" +
                "studentId=" + studentId +
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
