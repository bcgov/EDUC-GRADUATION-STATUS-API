package ca.bc.gov.educ.api.graduationstatus.model.transformer;

import ca.bc.gov.educ.api.graduationstatus.model.dto.Course;
import ca.bc.gov.educ.api.graduationstatus.model.dto.GraduationData;
import ca.bc.gov.educ.api.graduationstatus.model.entity.GraduationStatusEntity;
import ca.bc.gov.educ.api.graduationstatus.service.GraduationStatusService;
import ca.bc.gov.educ.api.graduationstatus.util.GraduationStatusUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GraduationStatusTransformer {

    @Autowired
    ModelMapper modelMapper;

    private static Logger logger = LoggerFactory.getLogger(GraduationStatusService.class);

    public GraduationData transformToDTO (GraduationStatusEntity graduationStatusEntity ) {
        GraduationData graduationData = modelMapper.map(graduationStatusEntity, GraduationData.class);
        graduationData.setStatusDate(GraduationStatusUtils.formatDate(graduationStatusEntity.getUpdatedTimestamp()));

        return graduationData;
    }

    public GraduationStatusEntity transformToEntity(GraduationData graduationData) {
        GraduationStatusEntity graduationStatusEntity = modelMapper.map(graduationData, GraduationStatusEntity.class);
        graduationStatusEntity.setUpdatedTimestamp(GraduationStatusUtils.parseDate(graduationData.getStatusDate()));
        return graduationStatusEntity;
    }

}
