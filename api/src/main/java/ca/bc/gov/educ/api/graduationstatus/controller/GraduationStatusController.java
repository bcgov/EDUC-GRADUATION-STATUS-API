package ca.bc.gov.educ.api.graduationstatus.controller;

import ca.bc.gov.educ.api.graduationstatus.model.dto.*;
import ca.bc.gov.educ.api.graduationstatus.service.GraduationStatusService;
import ca.bc.gov.educ.api.graduationstatus.util.GraduationStatusApiConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping (GraduationStatusApiConstants.GRADUATION_STATUS_API_ROOT_MAPPING)
public class GraduationStatusController {

    private static Logger logger = LoggerFactory.getLogger(GraduationStatusController.class);

    @Autowired
    GraduationStatusService gradStatusService;

    @GetMapping (GraduationStatusApiConstants.GRADUATION_STATUS_BY_PEN)
    public Student getResponse(@PathVariable String pen) {
        logger.debug("Graduation Status API called");
        return gradStatusService.getResponse(pen);
    }

    @PostMapping(GraduationStatusApiConstants.API_ROOT_MAPPING)
    public GraduationData createGraduationData(@RequestBody GraduationData graduationData) {
        logger.debug("#Create Graduation Data: " + graduationData.getPen());
        logger.debug("******GraduationData*****\n" + graduationData.toString());
        return gradStatusService.createGradData(graduationData);
    }
}
