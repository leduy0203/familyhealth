package familyhealth.service.impl;

import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.HouseHoldMapper;
import familyhealth.model.Household;
import familyhealth.model.dto.HouseholdDTO;
import familyhealth.repository.HouseholdRepository;
import familyhealth.service.IHouseholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseholdService implements IHouseholdService {
    private final HouseholdRepository householdRepository;

    @Override
    public Household getHousehold(Long id) {
        return householdRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.HOUSEHOLD_NOT_FOUND));
    }

    @Override
    public Household createHousehold(HouseholdDTO householdDTO) {
        Household household = HouseHoldMapper.convertToHousehold(householdDTO);
        return householdRepository.save(household);
    }

    @Override
    public Household updateHousehold(Long id, HouseholdDTO householdDTO) {
        Household existingHousehold  = getHousehold(id);
        Household updatedHousehold = HouseHoldMapper.convertToHousehold(householdDTO);
        updatedHousehold.setId(existingHousehold.getId());
        return householdRepository.save(updatedHousehold);
    }

    @Override
    public void deleteHousehold(Long id) {
        Household existingHousehold = getHousehold(id);
        existingHousehold.setIsActive(false);
        householdRepository.save(existingHousehold);
    }
}
