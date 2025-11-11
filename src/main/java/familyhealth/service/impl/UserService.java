package familyhealth.service.impl;

import familyhealth.common.Gender;
import familyhealth.common.Relation;
import familyhealth.mapper.HouseHoldMapper;
import familyhealth.model.*;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.HouseholdDTO;
import familyhealth.model.dto.MemberDTO;
import familyhealth.model.dto.UserDTO;
import familyhealth.repository.HouseholdRepository;
import familyhealth.repository.MemberRepository;
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

    //Tìm tài khoản theo id
    @Override
    public User getUser(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
    }


    @Override
    public User createUser(UserDTO userDTO) {
        if (this.userRepository.existsByPhone(userDTO.getPhone())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        User user = UserMapper.convertToUser(userDTO, role);

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUser(id);
        userRepository.delete(user);
    }

    //Tao user Doctor - chi Admin moi tao duoc
//    @Override
//    public User createUserDoctor(UserDTO userDTO, DoctorDTO doctorDTO) {
//        User user = createUser(userDTO);
//
//        Doctor doctor = doctorService.createDoctor(doctorDTO, user);
//
//        user.setProfileId(doctor.getId());
//        userRepository.save(user);
//
//        return user;
//    }
//
//    //Tao user Member
//    @Override
//    public User createUserMember(UserDTO userDTO, MemberDTO memberDTO, Long householdId) {
//        if (this.userRepository.existsByPhone(userDTO.getPhone())) {
//            throw new AppException(ErrorCode.USER_EXISTED);
//        }
//        Role role = roleRepository.findById(userDTO.getRoleId())
//                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
//        User user = UserMapper.convertToUser(userDTO, role);
//        user = userRepository.save(user);
//
//        Household household;
//        if (memberDTO.getRelation() == Relation.CHU_HO) {
//            household = householdService.createHousehold(new HouseholdDTO());
//            Member member = memberService.createMember(memberDTO, user, household);
//            household.setHouseheadId(member.getId());
//            household.setAddress(user.getAddress());
//            member.setHousehold(household);
//            memberRepository.save(member);
//            user.setProfileId(member.getId());
//        }
//
//        else {
//            long count;
//            household = householdService.getHousehold(householdId);
//            User headmember = userRepository.findById(household.getHouseheadId())
//                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//            Member member = new Member();
//            //Neu la Nam
//            if(headmember.getGender() == Gender.MALE) {
//                if (memberDTO.getRelation() == Relation.CHONG) {
//                    throw new AppException(ErrorCode.MALE_CANNOT_HAVE_HUSBAND);//Sua EX
//                } else if (memberDTO.getRelation() == Relation.VO && userDTO.getGender() == Gender.FEMALE) {
//                    count = memberRepository.countByHouseholdIdAndRelation(householdId, Relation.VO);
//                    if (count == 0) {
//                        member = memberService.createMember(memberDTO, user, household);
//                    } else {
//                        throw new AppException(ErrorCode.ALREADY_HAS_WIFE);//Sua ex
//                    }
//                }
//            }
//            //Neu la Nu
//            if (headmember.getGender() == Gender.FEMALE) {
//                if(memberDTO.getRelation() == Relation.VO){
//                    throw new AppException(ErrorCode.FEMALE_CANNOT_HAVE_WIFE);//Sua EX
//                }
//                else if (memberDTO.getRelation() == Relation.CHONG && userDTO.getGender() == Gender.MALE) {
//                    count = memberRepository.countByHouseholdIdAndRelation(householdId, Relation.CHONG);
//                    if (count == 0) {
//                        member = memberService.createMember(memberDTO, user, household);
//                    } else {
//                        throw new AppException(ErrorCode.ALREADY_HAS_HUSBAND);//Sua ex
//                    }
//                }else {
//                    throw new AppException(ErrorCode.ALREADY_HAS_HUSBAND);//Sua ex
//                }
//            }
//            household.setQuantity(household.getQuantity() + 1);
//            householdRepository.save(household);
//            user.setProfileId(member.getId());
//        }
//        return userRepository.save(user);
//    }
//
//
//
//    //Đăng nhập
//    @Override
//    public String login(String phoneNumber, String password) {
//        return "";
//    }
//
//    //Danh sách tài khoản
//    @Override
//    public List<User> getAllUsers() {
//        return this.userRepository.findAll();
//    }
//
//    //Cập nhật tài khaoản theo id
//    @Override
//    public User updateUser(Long id, UserDTO userDTO) {
//        return null;
//    }
//
}
