package familyhealth.mapper;

import familyhealth.model.Appointment;
import familyhealth.model.Doctor;
import familyhealth.model.MedicalResult;
import familyhealth.model.Member;
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

    public static MedicalResultResponse toResponse(MedicalResult mr) {
        if (mr == null) return null;

        MedicalResultResponse dto = new MedicalResultResponse();
        dto.setId(mr.getId());
        dto.setName(mr.getName());
        dto.setDiagnose(mr.getDiagnose());
        dto.setNote(mr.getNote());
        dto.setTotalMoney(mr.getTotalMoney());
        dto.setCreatedAt(mr.getCreatedAt());

        dto.setAppointment(toAppointmentInner(mr.getAppointment()));

        return dto;
    }

    private static MedicalResultResponse.Appointment toAppointmentInner(Appointment ap) {
        if (ap == null) return null;

        MedicalResultResponse.Appointment dto = new MedicalResultResponse.Appointment();

        dto.setId(ap.getId());
        dto.setTime(ap.getTime());
        dto.setStatus(ap.getStatus().name());
        dto.setNote(ap.getNote());
        dto.setLocation(ap.getLocation());

        dto.setDoctor(toDoctorInner(ap.getDoctor()));
        dto.setMember(toMemberInner(ap.getMember()));

        return dto;
    }

    private static MedicalResultResponse.Doctor toDoctorInner(Doctor d) {
        if (d == null) return null;

        MedicalResultResponse.Doctor dto = new MedicalResultResponse.Doctor();
        dto.setId(d.getId());
        dto.setFullname(d.getFullname());
        dto.setExpertise(d.getExpertise().name());
        return dto;
    }

    private static MedicalResultResponse.Member toMemberInner(Member m) {
        if (m == null) return null;

        MedicalResultResponse.Member dto = new MedicalResultResponse.Member();
        dto.setId(m.getId());
        dto.setFullname(m.getFullname());
        dto.setGender(m.getGender().name());
        dto.setRelation(m.getRelation().name());
        return dto;
    }
}
