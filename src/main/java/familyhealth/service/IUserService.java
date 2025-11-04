package familyhealth.service;

import familyhealth.model.Household;
import familyhealth.model.User;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.HouseholdDTO;
import familyhealth.model.dto.MemberDTO;
import familyhealth.model.dto.UserDTO;
import familyhealth.model.dto.request.UserRequestDTO;

import java.util.List;

public interface IUserService {
    User createUserDoctor(UserDTO userDTO, DoctorDTO doctorDTO);
    User createUserMember(UserDTO userDTO, MemberDTO memberDTO, HouseholdDTO householdDTO);
    String login(String phoneNumber, String password);
    User updateUser(Long id, UserDTO userDTO);
    User getUser(Long id);
    void deleteUser(Long id);
    List<User> getAllUsers();
}
