package familyhealth.mapper;

import familyhealth.common.MemberStatus;
import familyhealth.common.Relation;
import familyhealth.model.Household;
import familyhealth.model.Member;
import familyhealth.model.User;
import familyhealth.model.dto.MemberDTO;
import familyhealth.model.dto.request.MemberRegisterDTO;
import familyhealth.model.dto.request.UserRequestDTO;
import familyhealth.model.dto.response.MemberResponse;
import familyhealth.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.util.List;

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

    public static MemberResponse convertToMemberResponse(Member member) {
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setFullname(member.getFullname());
        response.setIdCard(member.getIdCard());
        response.setGender(member.getGender().name());
        response.setDateOfBirth(member.getDateOfBirth().atStartOfDay());
        response.setEmail(member.getEmail());
        response.setBhyt(member.getBhyt());

        List<MemberResponse.MedicalResultDTO> results = member.getAppointments()
                .stream()
                .filter(a -> a.getMedicalResult() != null)
                .map(a -> {
                    MemberResponse.MedicalResultDTO dto = new MemberResponse.MedicalResultDTO();
                    dto.setId(a.getMedicalResult().getId());
                    dto.setName(a.getMedicalResult().getName());
                    dto.setDiagnose(a.getMedicalResult().getDiagnose());
                    dto.setNote(a.getMedicalResult().getNote());
                    dto.setTotalMoney(a.getMedicalResult().getTotalMoney());
                    dto.setCreatedAt(a.getMedicalResult().getCreatedAt());
                    dto.setAppointmentTime(a.getTime());
                    dto.setDoctorName(a.getDoctor().getFullname());
                    return dto;
                }).toList();

        response.setMedicalResults(results);

        return response;
    }

}

