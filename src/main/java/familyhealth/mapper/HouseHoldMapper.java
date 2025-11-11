package familyhealth.mapper;

import familyhealth.model.Household;
import familyhealth.model.dto.HouseholdDTO;

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
}
