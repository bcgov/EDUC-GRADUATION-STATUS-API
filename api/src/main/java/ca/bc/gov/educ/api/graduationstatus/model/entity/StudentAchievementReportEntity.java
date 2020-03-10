package ca.bc.gov.educ.api.graduationstatus.model.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "graduation_status")
public class StudentAchievementReportEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "graduation_status_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID gradStatusId;

    @Lob
    @Column(name = "student_achievement_report", columnDefinition="CLOB")
    private String achievementReport;

    @Column(name = "pen", nullable = false)
    private String pen;

}