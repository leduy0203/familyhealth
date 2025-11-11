package familyhealth.mapper;

import familyhealth.model.Doctor;
import familyhealth.model.User;
import familyhealth.model.dto.DoctorDTO;

public class DoctorMapper {
    public static Doctor convertToDotor(DoctorDTO dto, User user) {
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
}
