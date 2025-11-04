package familyhealth.mapper;

import familyhealth.model.Household;
import familyhealth.model.Member;
import familyhealth.model.User;
import familyhealth.model.dto.MemberDTO;

public class MemberMapper {
    public static Member convertToMember(MemberDTO dto, User user, Household household) {
        if (dto == null) return null;

        return Member.builder()
                .relation(dto.getRelation())
                .bhyt(dto.getBhyt())
                .household(household)
                .user(user)
                .build();
    }
}
