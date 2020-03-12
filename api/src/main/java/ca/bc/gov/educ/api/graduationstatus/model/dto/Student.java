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
    private List<String> gradMessages;
    private List<String> requirementsMet;
    private List<String> requirementsNotMet;
    private List<AchievementDto> achievements;

    public Student() {

    }

    public Student (String pen){
        this.pen = pen;
        if ("126214493".compareTo(pen) == 0) {
            legalFirstName = "Betty";
            legalMiddleName = "";
            legalLastName = "Cotnam";
            graduationProgram = "2018";
            school = "King George Secondary";
            dob = "16-DEC-2002";
            sexCode = "F";
            genderCode = "F";
            dataSourceCode = "AB11";
            usualFirstName = "Cotnam";
            usualMiddleName = "";
            usualLastName = "Doe";
            email = "betty.cotnam@kinggeorge.ca";
            gradeCode = "11";
            citizenship = "C";
            address = "123 King George Ave, Victoria BC A1B2C3";
        }
        else if ("128201845".compareTo(pen) == 0) {
            legalFirstName = "Emily";
            legalMiddleName = "";
            legalLastName = "Fuller";
            graduationProgram = "2018";
            school = "King George Secondary";
            dob = "30-MAY-2003";
            sexCode = "F";
            genderCode = "F";
            dataSourceCode = "AB12";
            usualFirstName = "Emily";
            usualMiddleName = "";
            usualLastName = "Fuller";
            email = "emily.fuller@stellys.ca";
            gradeCode = "12";
            citizenship = "C";
            address = "456 Stellys, Victoria BC A1B2C3";
        }
        else if ("123456789".compareTo(pen) == 0) {
            legalFirstName = "Matthew";
            legalMiddleName = "Robert";
            legalLastName = "Timothy";
            graduationProgram = "2018";
            school = "Oak Bay High School";
            dob = "09-SEP-2004";
            sexCode = "M";
            genderCode = "M";
            dataSourceCode = "AB10";
            usualFirstName = "Matthew";
            usualMiddleName = "Robert";
            usualLastName = "Timothy";
            email = "matthew.timothy@oakbay.ca";
            gradeCode = "10";
            citizenship = "C";
            address = "987 Oak Street, Victoria BC A1B2C3";
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "pen='" + pen + '\'' +
                ", legalFirstName='" + legalFirstName + '\'' +
                ", legalMiddleName='" + legalMiddleName + '\'' +
                ", legalLastName='" + legalLastName + '\'' +
                ", graduationProgram='" + graduationProgram + '\'' +
                ", school='" + school + '\'' +
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
                ", citizenship='" + citizenship + '\'' +
                ", address='" + address + '\'' +
                ", achievements=" + achievements +
                ", gradMessages=" + gradMessages +
                '}';
    }
}
