package familyhealth.controller;

import familyhealth.model.Household;
import familyhealth.model.dto.HouseholdDTO;
import familyhealth.service.impl.HouseholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/households")
@RequiredArgsConstructor
public class HouseholdController {
    final private HouseholdService householdService;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getHousehold(@PathVariable Long id){
        try{
            Household household = householdService.getHousehold(id);
            return ResponseEntity.ok("Get Household " + household);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT_HOUSEHOLD')")
    public ResponseEntity<?> createHousehold(@RequestBody HouseholdDTO householdDTO){
        try{
            Household household = householdService.createHousehold(householdDTO);
            return ResponseEntity.ok("Create Household : " + household);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT_HOUSEHOLD')")
    public ResponseEntity<?> updateHousehold(@PathVariable Long id,
                                        @RequestBody HouseholdDTO householdDTO){
        try{
            Household household = householdService.updateHousehold(id, householdDTO);
            return ResponseEntity.ok("Update household : " + household);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteHousehold(@PathVariable Long id){
        try{
            householdService.deleteHousehold(id);
            return ResponseEntity.ok("Delete household : " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
