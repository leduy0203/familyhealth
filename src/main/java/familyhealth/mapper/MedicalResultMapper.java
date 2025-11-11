package familyhealth.mapper;

import familyhealth.model.Appointment;
import familyhealth.model.MedicalResult;
import familyhealth.model.dto.MedicalResultDTO;

public class MedicalResultMapper {
    public static MedicalResult convertToMedicalResult(MedicalResultDTO dto, Appointment appointment) {
        if (dto == null) return null;

        return MedicalResult.builder()
                .name(dto.getName())
                .note(dto.getNote())
                .diagnose(dto.getDiagnose())
                .totalMoney(dto.getTotalMoney())
                .createdAt(dto.getCreatedAt())
                .appointment(appointment)
                .build();
    }
}
