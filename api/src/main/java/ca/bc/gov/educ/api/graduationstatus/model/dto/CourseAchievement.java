package ca.bc.gov.educ.api.graduationstatus.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Component
public class CourseAchievement {

    private UUID courseAchievementId;
    private String pen;
    private String sessionDate;
    private Double finalPercent;
    private Double interimPercent;
    private String finalLetterGrade;
    private int credits;
    private UUID courseId;
    private String courseType;
    private String interimLetterGrade;

    @Override
    public String toString() {
        return "\nCourseAchievement{" +
                "courseAchievementId=" + courseAchievementId +
                ", pen='" + pen + '\'' +
                ", sessionDate=" + sessionDate +
                ", finalPercent=" + finalPercent +
                ", interimPercent=" + interimPercent +
                ", finalLetterGrade='" + finalLetterGrade + '\'' +
                ", credits=" + credits +
                ", courseId=" + courseId +
                ", courseType=" + courseType +
                ", interimLetterGrade='" + interimLetterGrade + '\'' +
                "}";
    }
}
