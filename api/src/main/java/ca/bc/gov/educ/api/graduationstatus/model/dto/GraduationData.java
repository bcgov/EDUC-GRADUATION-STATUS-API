package ca.bc.gov.educ.api.graduationstatus.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Data
@Component
public class GraduationData {

    public GraduationData() {
        studentGradData = new StringBuffer();
        transcriptReport = new StringBuffer();
        achievementReport = new StringBuffer();
    }

    private UUID gradStatusId;
    private String pen = "";
    private StringBuffer studentGradData;
    private StringBuffer transcriptReport;
    private StringBuffer achievementReport;
    private String statusDate = "";

}
