package ca.bc.gov.educ.api.graduationstatus.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Data
@Component
public class GraduationData {

    private UUID gradStatusId;
    private String pen;
    private String studentGradData;
    private String transcriptReport;
    private String achievementReport;
    private String statusDate;

}
