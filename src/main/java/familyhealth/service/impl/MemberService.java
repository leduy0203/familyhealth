package familyhealth.service.impl;

import familyhealth.common.MemberStatus;
import familyhealth.common.Relation;
import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.MemberMapper;
import familyhealth.mapper.UserMapper;
import familyhealth.model.Household;
import familyhealth.model.Member;
import familyhealth.model.Role;
import familyhealth.model.User;
import familyhealth.model.dto.MemberDTO;
import familyhealth.model.dto.request.MemberRegisterDTO;
import familyhealth.model.dto.response.PageResponse;
import familyhealth.model.dto.response.UserResponse;
import familyhealth.repository.MemberRepository;
import familyhealth.repository.UserRepository;
import familyhealth.service.IMemberService;
import familyhealth.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "MEMBER_SERVICE")
public class MemberService implements IMemberService {
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserService userService;

    @Override
    public PageResponse<List<Member>> getFamilyMembers(String[] search, Pageable pageable) {

        String phone = SecurityUtils.getCurrentLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        User currentUser = userService.getUserByPhone(phone);

        Optional<Member> currentUserMember = memberRepository.findByUserId(currentUser.getId());

        if (currentUserMember.isEmpty()) {
            throw new AppException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Long householdId = currentUserMember.get().getHousehold().getId();

        if (householdId == null) {
            throw new AppException(ErrorCode.HOUSEHOLD_NOT_FOUND);
        }

        Page<Member> members =  memberRepository.findActiveMembersByHousehold(householdId, pageable);

        PageResponse.Meta meta = new PageResponse.Meta();
        meta.setPage(members.getNumber());
        meta.setPageSize(members.getSize());
        meta.setPages(members.getTotalPages());
        meta.setTotal(members.getTotalElements());

        return PageResponse.<List<Member>>builder()
                .meta(meta)
                .result(members.getContent())
                .build();
    }

    @Override
    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Member createMember(MemberRegisterDTO request) {

        String currentUserPhone = SecurityUtils.getCurrentLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        User currentUser = userService.getUserByPhone(currentUserPhone);

        Member currentMember = memberRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));

        Household household = currentMember.getHousehold();

        User newUser = null;

        if (request.getUserAccount() != null && isValidUserAccount(request.getUserAccount())) {
            Role role = roleService.getRole(request.getUserAccount().getRoleId());
            newUser = this.userRepository.save(UserMapper.convertToUser(request.getUserAccount() , role));
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
    @Transactional(rollbackOn = Exception.class)
    public void deleteMember(Long id) {
        log.info("Deleting member with id: {}", id);

        Member member = getMember(id);
        if (member.getRelation() == Relation.CHU_HO){
            throw new AppException(ErrorCode.CANNOT_DELETE_HEAD_OF_HOUSEHOLD);
        }
        member.setMemberStatus(MemberStatus.INACTIVE);
        this.memberRepository.save(member);

        User user = member.getUser();
        if (user != null){
            user.setIsActive(false);
            this.userRepository.save(user);
        }

    }


}
