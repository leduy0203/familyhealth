package familyhealth.controller;

import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.response.AppointmentResponse;
import familyhealth.model.dto.response.PageResponse;
import familyhealth.utils.MessageKey;
import familyhealth.model.Appointment;
import familyhealth.model.dto.AppointmentDTO;
import familyhealth.model.dto.response.ApiResponse;
import familyhealth.service.IAppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final IAppointmentService appointmentService;

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
    public ResponseEntity<?> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO){
        try{

            Appointment appointment = appointmentService.createAppointment(appointmentDTO);

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(CREATED.value())
                    .message(MessageKey.CREATE_APPOINTMENT_SUCCESS)
                    .data(appointment.getId())
                    .build()
            );

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


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAppointment(
            @RequestParam(required = false) String[] search
            , Pageable pageable){
        try{

            PageResponse<List<AppointmentResponse>> pageResponse = appointmentService
                    .getAllAppointmentService(search, pageable);

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(OK.value())
                    .message(MessageKey.GET_LIST_APPOINTMENT_SUCCESS)
                    .data(pageResponse)
                    .build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
