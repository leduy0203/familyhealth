package vn.familyhealth.mapper;

import vn.familyhealth.model.User;
import vn.familyhealth.model.dto.UserDTO;

public class UserMapper {

    public static UserDTO userDTO(User user){
        return UserDTO.builder()
                .username(user.getEmail())
                .password(user.getEmail())
                .build();
    }
}
