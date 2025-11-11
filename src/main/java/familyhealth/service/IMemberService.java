package familyhealth.service;

import familyhealth.model.Household;
import familyhealth.model.Member;
import familyhealth.model.User;
import familyhealth.model.dto.MemberDTO;

public interface IMemberService {
        Member getMember(Long id);
        Member createMember(MemberDTO memberDTO);
        Member updateMember(Long id, MemberDTO memberDTO);
        void deleteMember(Long id);
}
