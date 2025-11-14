package familyhealth.mapper;

import familyhealth.model.Doctor;
import familyhealth.model.User;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.request.DoctorRegisterDTO;

public class DoctorMapper {
    public static Doctor convertToDoctor(DoctorRegisterDTO dto, User user) {
        if (dto == null) return null;

        return Doctor.builder()
                .fullname(dto.getFullname())
                .idCard(dto.getIdCard())
                .address(dto.getAddress())
                .gender(dto.getGender())
                .dateOfBirth(dto.getDateOfBirth())
                .email(dto.getEmail())
                .user(user)
                .expertise(dto.getExpertise())
                .bio(dto.getBio())
                .build();
    }

    public static void mapDtoToEntity(DoctorDTO dto, Doctor targetDoctor) {
        if (dto == null || targetDoctor == null) {
            return;
        }
        targetDoctor.setFullname(dto.getFullname());
        targetDoctor.setIdCard(dto.getIdCard());
        targetDoctor.setAddress(dto.getAddress());
        targetDoctor.setGender(dto.getGender());
        targetDoctor.setDateOfBirth(dto.getDateOfBirth());
        targetDoctor.setEmail(dto.getEmail());
        targetDoctor.setExpertise(dto.getExpertise());
        targetDoctor.setBio(dto.getBio());
    }

    public static DoctorDTO convertToDoctorDTO(Doctor doctor) {
        if (doctor == null) return null;

        return DoctorDTO.builder()
                .id(doctor.getId())
                .fullname(doctor.getFullname())
                .idCard(doctor.getIdCard())
                .address(doctor.getAddress())
                .gender(doctor.getGender())
                .dateOfBirth(doctor.getDateOfBirth())
                .email(doctor.getEmail())
                .expertise(doctor.getExpertise())
                .bio(doctor.getBio())
                .build();
    }
}
