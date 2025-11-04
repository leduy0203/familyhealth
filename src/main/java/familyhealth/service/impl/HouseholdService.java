package familyhealth.service.impl;

import familyhealth.mapper.DoctorMapper;
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
                .orElseThrow(()-> new RuntimeException("House Hold not found"));
    }

    @Override
    public Household createHousehold(HouseholdDTO householdDTO) {
        Household household = HouseHoldMapper.convertToHousehold(householdDTO);
        return householdRepository.save(household);
    }

    @Override
    public Household updateHousehold(Long id, HouseholdDTO householdDTO) {
        return null;
    }

    @Override
    public void deleteHousehold(Long id) {

    }
}
