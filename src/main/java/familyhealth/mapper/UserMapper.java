package familyhealth.mapper;

import familyhealth.model.User;
import familyhealth.model.dto.UserDTO;
import familyhealth.model.Role;

import java.time.LocalDateTime;

public class UserMapper {

    public static UserDTO convertToUserDTO(User user){
        return UserDTO.builder()
                .phone(user.getPhone())
                .password(user.getPassword())
                .build();
    }

    public static User convertToUser(UserDTO dto, Role role) {
        if (dto == null) return null;

        return User.builder()
                .phone(dto.getPhone())
                .password(dto.getPassword())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .createdAt(LocalDateTime.now())
                .role(role)
                .build();
    }

}
