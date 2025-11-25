package familyhealth.controller;

import familyhealth.Utils.MessageKey;
import familyhealth.model.Appointment;
import familyhealth.model.dto.AppointmentDTO;
import familyhealth.model.dto.response.ApiResponse;
import familyhealth.service.impl.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAppointment(@PathVariable Long id){
        try{
            Appointment appointment = appointmentService.getAppointment(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .code(OK.value())
                    .message(MessageKey.GET_APPOINTMENT_SUCCESS)
                    .data(appointment)
                    .build()
            );
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT', 'PATIENT_HOUSEHOLD')")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO appointmentDTO){
        try{
            Appointment appointment = appointmentService.createAppointment(appointmentDTO);
            return ResponseEntity.ok("Create Appointment : " + appointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id,
                                        @RequestBody AppointmentDTO appointmentDTO){
        try{
            Appointment appointment = appointmentService.updateAppointment(id, appointmentDTO);
            return ResponseEntity.ok("Update Appointment : " + appointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id){
        try{
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok("Delete Appointment : " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
