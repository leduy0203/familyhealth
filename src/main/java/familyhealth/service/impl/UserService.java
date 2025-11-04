package familyhealth.service.impl;

import familyhealth.common.Relation;
import familyhealth.model.*;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.HouseholdDTO;
import familyhealth.model.dto.MemberDTO;
import familyhealth.model.dto.UserDTO;
import familyhealth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.UserMapper;
import familyhealth.repository.UserRepository;
import familyhealth.service.IUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DoctorService doctorService;
    private final MemberService memberService;
    private final HouseholdService householdService;

    //Tìm tài khoản theo id
    @Override
    public User getUser(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    //Tao user Member
    @Override
    public User createUserMember(UserDTO userDTO, MemberDTO memberDTO, HouseholdDTO householdDTO) {
        if (this.userRepository.existsByPhone(userDTO.getPhone())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = UserMapper.convertToUser(userDTO, role);
        user = userRepository.save(user);

        Household household = new Household();
        if (memberDTO.getRelation() == Relation.CHU_HO) {
            Member member = memberService.createMember(memberDTO, user, household);
        } else {

        }

        //Nếu là chủ hộ
        //Tạo mới house hold

        //Nếu relation khác chủ hộ
        //Thêm bình thường


        Member member = memberService.createMember(memberDTO, user, household);
        user.setProfileId(member.getId());
        userRepository.save(user);

        return user;
    }

    //Tao user Doctor - chi Admin moi tao duoc
    @Override
    public User createUserDoctor(UserDTO userDTO, DoctorDTO doctorDTO) {
        if (this.userRepository.existsByPhone(userDTO.getPhone())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = UserMapper.convertToUser(userDTO, role);
        user = userRepository.save(user);

        Doctor doctor = doctorService.createDoctor(doctorDTO, user);

        user.setProfileId(doctor.getId());
        userRepository.save(user);

        return user;
    }

    //Đăng nhập
    @Override
    public String login(String phoneNumber, String password) {
        return "";
    }

    //Danh sách tài khoản
    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    //Cập nhật tài khaoản theo id
    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        return null;
    }

    //Xóa mềm - thây đồi active thành 0
    @Override
    public void deleteUser(Long id) {
    }
}
