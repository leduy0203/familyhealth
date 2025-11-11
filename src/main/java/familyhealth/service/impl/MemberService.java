package familyhealth.service.impl;

import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.MemberMapper;
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
    private final UserService userService;
    private final HouseholdService householdService;

    @Override
    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public Member createMember(MemberDTO memberDTO) {
        User existingUser = null;
        if (memberDTO.getUserId() != null) {
            existingUser = userService.getUser(memberDTO.getUserId());
        }
        Household existingHousehold = householdService.getHousehold(memberDTO.getHouseholdId());
        Member member = MemberMapper.convertToMember(memberDTO, existingUser, existingHousehold);
        return memberRepository.save(member);
    }

    @Override
    public Member updateMember(Long id, MemberDTO memberDTO) {
        Member member = getMember(id);
        Member updateMember = MemberMapper.convertToMember(memberDTO, member.getUser(), member.getHousehold());
        updateMember.setId(member.getId());
        return memberRepository.save(updateMember);
    }

    @Override
    public void deleteMember(Long id) {
        Member member = getMember(id);
        userService.deleteUser(member.getUser().getId());
        memberRepository.delete(member);
    }


}
