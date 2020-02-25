package ca.bc.gov.educ.api.graduationstatus.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Component
public class AchievementDto {

    private UUID courseAchievementId;
    private String pen;
    private String sessionDate;
    private Double finalPercent;
    private Double interimPercent;
    private String finalLetterGrade;
    private int credits;
    private String courseType;
    private String interimLetterGrade;
    private int gradRequirementMet;
    private CourseDto course;

    @Override
    public String toString() {
        return "\nAchievementDto{" +
                "courseAchievementId=" + courseAchievementId +
                ", pen='" + pen + '\'' +
                ", sessionDate='" + sessionDate + '\'' +
                ", finalPercent=" + finalPercent +
                ", interimPercent=" + interimPercent +
                ", finalLetterGrade='" + finalLetterGrade + '\'' +
                ", credits=" + credits +
                ", course=" + course +
                ", courseType='" + courseType + '\'' +
                ", interimLetterGrade='" + interimLetterGrade + '\'' +
                '}';
    }
}
