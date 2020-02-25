package ca.bc.gov.educ.api.graduationstatus.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ProgramCodeDto {
    private String programCode;
    private String programName;
    private String programStartDate;
    private String programEndDate;

    @Override
    public String toString() {
        return "\nProgramCodeDto{" +
                "programCode='" + programCode + '\'' +
                ", programName='" + programName + '\'' +
                ", programStartDate='" + programStartDate + '\'' +
                ", programEndDate='" + programEndDate + '\'' +
                '}';
    }
}
