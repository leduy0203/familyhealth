package vn.familyhealth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.familyhealth.exception.AppException;
import vn.familyhealth.exception.ErrorCode;
import vn.familyhealth.model.User;
import vn.familyhealth.repository.UserRepository;
import vn.familyhealth.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
    }


}
