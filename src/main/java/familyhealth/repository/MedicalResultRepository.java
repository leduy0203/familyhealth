package familyhealth.repository;

import familyhealth.model.MedicalResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalResultRepository extends JpaRepository<MedicalResult, Long> {
}
