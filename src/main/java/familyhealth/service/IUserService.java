package familyhealth.service;

import familyhealth.model.Household;
import familyhealth.model.User;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.HouseholdDTO;
import familyhealth.model.dto.MemberDTO;
import familyhealth.model.dto.UserDTO;
import familyhealth.model.dto.request.UserRequestDTO;
import familyhealth.model.dto.response.PageResponse;
import familyhealth.model.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    User createUser(UserDTO userDTO);
//    String login(String phoneNumber, String password);
//    User updateUser(Long id, UserDTO userDTO);
    User getUser(Long id);
    void deleteUser(Long id);
    PageResponse<List<UserResponse>> getAllUsers(String[] search, Pageable pageable);
}
