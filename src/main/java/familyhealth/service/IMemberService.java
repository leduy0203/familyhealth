package familyhealth.service;

import familyhealth.model.Member;
import familyhealth.model.dto.MemberDTO;
import familyhealth.model.dto.request.MemberRegisterDTO;
import familyhealth.model.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMemberService {
        Member getMember(Long id);
        Member createMember(MemberRegisterDTO request);
        Member updateMember(Long id, MemberDTO memberDTO);
        void deleteMember(Long id);
        PageResponse<List<Member>> getFamilyMembers(String[] search,
                                              Pageable pageable);
}
