package familyhealth.mapper;

import familyhealth.model.User;
import familyhealth.model.dto.UserDTO;
import familyhealth.model.dto.request.UserRequestDTO;

public class UserMapper {

    public static UserDTO convertToUserDTO(User user){
        return UserDTO.builder()
                .phone(user.getEmail())
                .password(user.getEmail())
                .build();
    }

    public static User convertToUser(UserRequestDTO userRequestDTO) {
        return User.builder()
                .email(userRequestDTO.getEmail())
                .password(userRequestDTO.getPassword())
                .fullName(userRequestDTO.getFullName())
                .phone(userRequestDTO.getPhone())
                .build();
    }

}
