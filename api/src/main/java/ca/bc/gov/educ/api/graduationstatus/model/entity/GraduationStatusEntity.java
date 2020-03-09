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
public class GraduationStatusEntity {

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

    @Column(name = "pen", nullable = false)
    private String pen;

    @Lob
    @Column(name = "student_grad_data", columnDefinition="CLOB")
    private String studentGradData;

    @Lob
    @Column(name = "student_transcript_report", columnDefinition="CLOB")
    private String transcriptReport;

    @Lob
    @Column(name = "student_achievement_report", columnDefinition="CLOB")
    private String achievementReport;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_timestamp", nullable = false)
    private Date createdTimestamp;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @Column(name = "updated_timestamp", nullable = false)
    private Date updatedTimestamp;

}