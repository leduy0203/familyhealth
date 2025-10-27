package vn.familyhealth.service;

import vn.familyhealth.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);
}
