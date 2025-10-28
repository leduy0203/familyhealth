package vn.familyhealth.service;

import vn.familyhealth.model.User;
import vn.familyhealth.model.dto.request.UserRequestDTO;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User createUser(UserRequestDTO user);
}
