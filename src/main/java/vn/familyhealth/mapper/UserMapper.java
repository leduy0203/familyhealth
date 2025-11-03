package vn.familyhealth.mapper;

import vn.familyhealth.model.User;
import vn.familyhealth.model.dto.UserDTO;
import vn.familyhealth.model.dto.request.UserRequestDTO;

public class UserMapper {

    public static UserDTO convertToUserDTO(User user){
        return UserDTO.builder()
                .username(user.getEmail())
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
