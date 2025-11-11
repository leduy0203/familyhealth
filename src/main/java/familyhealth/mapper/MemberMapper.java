package familyhealth.mapper;

import familyhealth.model.Household;
import familyhealth.model.Member;
import familyhealth.model.User;
import familyhealth.model.dto.MemberDTO;
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
}
