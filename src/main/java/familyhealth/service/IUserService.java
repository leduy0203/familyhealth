package familyhealth.service;

import familyhealth.model.User;
import familyhealth.model.dto.request.UserRequestDTO;
import familyhealth.model.dto.response.PageResponse;
import familyhealth.model.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    User createUser(UserRequestDTO request);
    User getUser(Long id);
    void deleteUser(Long id);
    PageResponse<List<UserResponse>> getAllUsers(String[] search, Pageable pageable);
}
