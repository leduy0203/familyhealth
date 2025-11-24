package familyhealth.service.impl;

import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.MemberMapper;
import familyhealth.mapper.UserMapper;
import familyhealth.model.Household;
import familyhealth.model.Member;
import familyhealth.model.User;
import familyhealth.model.dto.MemberDTO;
import familyhealth.model.dto.request.MemberRegisterDTO;
import familyhealth.repository.MemberRepository;
import familyhealth.repository.UserRepository;
import familyhealth.service.IMemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements IMemberService {
    private final MemberRepository memberRepository;
    private final UserService userService;
    private final HouseholdService householdService;
    private final UserRepository userRepository;

    @Override
    public List<Member> getFamilyMembers() {
        Long userId = 1L; // GetCurrentUserId.get();
        Optional<Member> currentUserMember = memberRepository.findByUserId(userId);

        if (currentUserMember.isEmpty()) {
            throw new AppException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Long householdId = currentUserMember.get().getHousehold().getId();

        if (householdId == null) {
            throw new AppException(ErrorCode.HOUSEHOLD_NOT_FOUND);
        }

        return memberRepository.findAllByHouseholdId(householdId);
    }

    @Override
    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Member createMember(MemberRegisterDTO request) {
        Long currentUserId = 1L; // GetCurrentUserId.get();

        Member currentMember = memberRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));

        Household household = currentMember.getHousehold();

        User newUser = null;

        if (request.getUserAccount() != null && isValidUserAccount(request.getUserAccount())) {
            newUser = this.userRepository.save(UserMapper.convertToUser(request.getUserAccount()));
        }

        Member newMember = MemberMapper.convertToMember(request, newUser, household);

        return memberRepository.save(newMember);
    }

    private boolean isValidUserAccount(MemberRegisterDTO.UserAccount userAccount) {
        String phone = userAccount.getPhone();
        String password = userAccount.getPassword();

        return phone != null && !phone.isBlank() && password != null && !password.isBlank();
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
