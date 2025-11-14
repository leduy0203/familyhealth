package familyhealth.mapper;

import familyhealth.model.User;
import familyhealth.model.dto.UserDTO;
import familyhealth.model.Role;
import familyhealth.model.dto.request.DoctorRegisterDTO;

import java.time.LocalDateTime;

public class UserMapper {

    public static UserDTO convertToUserDTO(User user){
        return UserDTO.builder()
                .phone(user.getPhone())
                .password(user.getPassword())
                .build();
    }

    public static User convertToUser(DoctorRegisterDTO request, Role role) {
        if (request == null) return null;

        return User.builder()
                .phone(request.getPhone())
                .password(request.getPassword())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .createdAt(LocalDateTime.now())
                .role(role)
                .build();
    }

    public static User convertToUser(UserDTO request, Role role) {
        if (request == null) return null;

        return User.builder()
                .phone(request.getPhone())
                .password(request.getPassword())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .createdAt(LocalDateTime.now())
                .role(role)
                .build();
    }

}
