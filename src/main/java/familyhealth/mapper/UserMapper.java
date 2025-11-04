package familyhealth.mapper;

import familyhealth.model.User;
import familyhealth.model.dto.UserDTO;
import familyhealth.model.Role;

public class UserMapper {

    public static UserDTO convertToUserDTO(User user){
        return UserDTO.builder()
                .phone(user.getEmail())
                .password(user.getEmail())
                .build();
    }

    public static User convertToUser(UserDTO dto, Role role) {
        if (dto == null) return null;

        return User.builder()
                .phone(dto.getPhone())
                .password(dto.getPassword())
                .fullName(dto.getFullName())
                .gender(dto.getGender())
                .dateOfBirth(dto.getDateOfBirth() != null ? dto.getDateOfBirth().atStartOfDay() : null)
                .email(dto.getEmail())
                .cccd(dto.getCccd())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .profileId(dto.getProfileId())
                .role(role)
                .build();
    }

}
