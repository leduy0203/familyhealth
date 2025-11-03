package familyhealth.service;

import familyhealth.model.User;
import familyhealth.model.dto.request.UserRequestDTO;

import java.util.List;

public interface IUserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User createUser(UserRequestDTO user);
}
