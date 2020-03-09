package ca.bc.gov.educ.api.graduationstatus.repository;

import ca.bc.gov.educ.api.graduationstatus.model.entity.GraduationStatusEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GraduationStatusRepository extends CrudRepository<GraduationStatusEntity, UUID> {

    GraduationStatusEntity findByPen (String pen);

}
