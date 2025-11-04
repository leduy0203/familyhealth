package familyhealth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.UserMapper;
import familyhealth.model.User;
import familyhealth.model.dto.request.UserRequestDTO;
import familyhealth.repository.UserRepository;
import familyhealth.service.IUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

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

    @Override
    public User createUser(UserRequestDTO user) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return this.userRepository.save(UserMapper.convertToUser(user));
    }
}
