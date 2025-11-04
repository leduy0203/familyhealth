package familyhealth.service.impl;

import familyhealth.mapper.DoctorMapper;
import familyhealth.mapper.MemberMapper;
import familyhealth.model.Doctor;
import familyhealth.model.Household;
import familyhealth.model.Member;
import familyhealth.model.User;
import familyhealth.model.dto.MemberDTO;
import familyhealth.repository.MemberRepository;
import familyhealth.service.IMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements IMemberService {
    private final MemberRepository memberRepository;

    @Override
    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Member not found"));
    }

    @Override
    public Member createMember(MemberDTO memberDTO, User user, Household household) {
        Member member = MemberMapper.convertToMember(memberDTO, user, household);
        return memberRepository.save(member);
    }

    @Override
    public Member updateMember(Long id, MemberDTO memberDTO) {
        return null;
    }

    @Override
    public void deleteMember(Long id) {

    }
}
