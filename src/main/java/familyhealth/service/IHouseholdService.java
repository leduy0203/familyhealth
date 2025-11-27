package familyhealth.service;

import familyhealth.model.Household;
import familyhealth.model.dto.HouseholdDTO;
import familyhealth.model.dto.response.HouseHoldResponse;

import java.util.List;

public interface IHouseholdService {
    Household getHousehold(Long id);
    Household createHousehold(HouseholdDTO householdDTO);
    Household updateHousehold(Long id, HouseholdDTO householdDTO);
    void deleteHousehold(Long id);

    List<HouseHoldResponse> getALlHouseholds();
}
