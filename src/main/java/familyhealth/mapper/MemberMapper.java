package familyhealth.mapper;

import familyhealth.common.MemberStatus;
import familyhealth.common.Relation;
import familyhealth.model.Household;
import familyhealth.model.Member;
import familyhealth.model.User;
import familyhealth.model.dto.MemberDTO;
import familyhealth.model.dto.request.MemberRegisterDTO;
import familyhealth.model.dto.request.UserRequestDTO;
import familyhealth.repository.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MemberMapper {


    public static Member convertToMember(MemberDTO dto, User user, Household household) {
        if (dto == null) return null;

        return Member.builder()
                .fullname(dto.getFullname())
                .idCard(dto.getIdCard())
                .address(dto.getAddress())
                .gender(dto.getGender())
                .dateOfBirth(dto.getDateOfBirth())
                .email(dto.getEmail())
                .relation(dto.getRelation())
                .bhyt(dto.getBhyt())
                .household(household)
                .user(user)
                .build();
    }

    public static Member convertToMember(UserRequestDTO request , Household household , User user) {

        UserRequestDTO.MemberInfo dto = request.getMemberInfo();

        if (dto == null) return null;

        return Member.builder()
                .fullname(dto.getFullName())
                .idCard(dto.getCccd())
                .address(dto.getAddress())
                .gender(dto.getGender())
                .bhyt(dto.getBhyt())
                .memberStatus(MemberStatus.ACTIVE)
                .dateOfBirth(dto.getDateOfBirth())
                .relation(Relation.CHU_HO)
                .household(household)
                .user(user)
                .build();
    }

    public static Member convertToMember(MemberRegisterDTO request, User newUser, Household household) {

        if (request == null) return null;

        return Member.builder()
                .fullname(request.getFullname())
                .idCard(request.getIdCard())
                .address(request.getAddress())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .relation(request.getRelation())
                .bhyt(request.getBhyt())
                .household(household)
                .memberStatus(MemberStatus.ACTIVE)
                .user(newUser)
                .build();
    }
}

