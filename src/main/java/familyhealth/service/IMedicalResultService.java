package familyhealth.service;

import familyhealth.model.MedicalResult;
import familyhealth.model.dto.MedicalResultDTO;

public interface IMedicalResultService {
    MedicalResult getMedicalResult(Long id);
    MedicalResult createMedicalResult(MedicalResultDTO MedicalResultDTO);
    MedicalResult updateMedicalResult(Long id, MedicalResultDTO MedicalResultDTO);
    void deleteMedicalResult(Long id);
}
