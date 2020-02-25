package ca.bc.gov.educ.api.graduationstatus.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ProgramRuleDto {
    private int requirementCode;
    private String requirementName;
    private int requiredCredits;
    private String notMetDescription;
    private ProgramCodeDto programCode;
    private String requirementType;
    private String activeFlag;
    private int requiredLevel;

    @Override
    public String toString() {
        return "\nProgramRule{" +
                "requirementCode=" + requirementCode +
                ", requirementName='" + requirementName + '\'' +
                ", requiredCredits=" + requiredCredits +
                ", notMetDescription='" + notMetDescription + '\'' +
                ", programCode='" + programCode + '\'' +
                ", requirementType='" + requirementType + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                ", requiredLevel=" + requiredLevel +
                '}';
    }
}
