package familyhealth.controller;

import familyhealth.model.MedicalResult;
import familyhealth.model.dto.MedicalResultDTO;
import familyhealth.service.IMedicalResultService;
import familyhealth.service.impl.MedicalResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medical_results")
public class MedicalResultController {
    private final IMedicalResultService medicalResultService;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMedicalResult(@PathVariable Long id){
        try{
            MedicalResult medicalResult = medicalResultService.getMedicalResult(id);
            return ResponseEntity.ok(medicalResult);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> creatMedicalResult(@RequestBody MedicalResultDTO medicalResultDTO){
        try{
            MedicalResult medicalResult = medicalResultService.createMedicalResult(medicalResultDTO);
            return ResponseEntity.ok(medicalResult);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateMedicalResult(@PathVariable Long id,
                                        @RequestBody MedicalResultDTO medicalResultDTO){
        try{
            MedicalResult medicalResult = medicalResultService.updateMedicalResult(id, medicalResultDTO);
            return ResponseEntity.ok("Update MedicalResult : " + medicalResult);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMedicalResult(@PathVariable Long id){
        try{
            medicalResultService.deleteMedicalResult(id);
            return ResponseEntity.ok("Delete MedicalResult : " + id);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
