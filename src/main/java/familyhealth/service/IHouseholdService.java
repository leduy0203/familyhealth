package familyhealth.service;

import familyhealth.model.Household;
import familyhealth.model.dto.HouseholdDTO;

public interface IHouseholdService {
    Household getHousehold(Long id);
    Household createHousehold(HouseholdDTO householdDTO);
    Household updateHousehold(Long id, HouseholdDTO householdDTO);
    void deleteHousehold(Long id);
}
