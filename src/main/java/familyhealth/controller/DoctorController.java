package familyhealth.controller;

import familyhealth.model.Doctor;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.service.impl.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getDoctor(@PathVariable Long id){
        try{
            Doctor doctor = doctorService.getDoctor(id);
            return ResponseEntity.ok("Get Doctor : " + doctor);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> creatDoctor(@RequestBody DoctorDTO doctorDTO){
        try {
            Doctor doctor = doctorService.createDoctor(doctorDTO);
            return ResponseEntity.ok("Created Doctor: " + doctor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable Long id,
                                        @RequestBody DoctorDTO doctorDTO){
        try{
            Doctor doctor = doctorService.updateDoctor(id, doctorDTO);
            return ResponseEntity.ok("Update Doctor : " + doctor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id){
        try{
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok("Delete Doctor : " + id);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
