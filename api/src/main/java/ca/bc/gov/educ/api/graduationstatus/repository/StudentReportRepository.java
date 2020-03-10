package ca.bc.gov.educ.api.graduationstatus.repository;

import ca.bc.gov.educ.api.graduationstatus.model.entity.GraduationStatusEntity;
import ca.bc.gov.educ.api.graduationstatus.model.entity.StudentAchievementReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentReportRepository extends JpaRepository<StudentAchievementReportEntity, UUID> {

    @Query(value = "SELECT g.achievementReport FROM StudentAchievementReportEntity g WHERE g.pen = :pen")
    String findStudentAchievementReportEntity(@Param("pen") String pen);

}
