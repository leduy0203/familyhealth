package familyhealth.controller;

import familyhealth.model.Appointment;
import familyhealth.model.dto.AppointmentDTO;
import familyhealth.service.impl.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

        @GetMapping("/{id}")
    public ResponseEntity<String> getAppointment(@PathVariable Long id){
        try{
            Appointment appointment = appointmentService.getAppointment(id);
            return ResponseEntity.ok("Get Appointment : " + appointment);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO appointmentDTO){
        try{
            Appointment appointment = appointmentService.createAppointment(appointmentDTO);
            return ResponseEntity.ok("Create Appointment : " + appointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id,
                                        @RequestBody AppointmentDTO appointmentDTO){
        try{
            Appointment appointment = appointmentService.updateAppointment(id, appointmentDTO);
            return ResponseEntity.ok("Update Appointment : " + appointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id){
        try{
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok("Delete Appointment : " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
