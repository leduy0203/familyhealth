package familyhealth.mapper;

import familyhealth.model.Household;
import familyhealth.model.Member;
import familyhealth.model.dto.HouseholdDTO;
import familyhealth.model.dto.request.UserRequestDTO;
import familyhealth.model.dto.response.HouseHoldResponse;

public class HouseHoldMapper {
    public static Household convertToHousehold(HouseholdDTO dto) {
        if (dto == null) return null;

        return Household.builder()
                .househeadId(dto.getHouseheadId())
                .address(dto.getAddress())
                .quantity(dto.getQuantity() != null ? dto.getQuantity() : 1)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
    }

    public static Household convertToHousehold(UserRequestDTO request) {
        if (request == null) return null;

        return Household.builder()
                .address(request.getMemberInfo().getAddress())
                .quantity(1)
                .isActive(true)
                .build();
    }

    public static HouseHoldResponse toResponse(Household household) {
        return HouseHoldResponse.builder()
                .id(household.getId())
                .househeadId(household.getHouseheadId())
                .address(household.getAddress())
                .quantity(household.getQuantity())
                .isActive(household.getIsActive())
                .members(
                        household.getMember().stream()
                                .map(HouseHoldMapper::toMemberResponse)
                                .toList()
                )
                .build();
    }

    private static HouseHoldResponse.MemberResponse toMemberResponse(Member member) {
        return HouseHoldResponse.MemberResponse.builder()
                .id(member.getId())
                .fullName(member.getFullname())
                .relation(member.getRelation())
                .bhyt(member.getBhyt())
                .memberStatus(member.getMemberStatus())
                .build();
    }
}
