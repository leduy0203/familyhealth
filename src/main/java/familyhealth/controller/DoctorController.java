package familyhealth.controller;

import familyhealth.Utils.MessageKey;
import familyhealth.mapper.DoctorMapper;
import familyhealth.model.Doctor;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.request.DoctorRegisterDTO;
import familyhealth.model.dto.response.ApiResponse;
import familyhealth.model.dto.response.PageResponse;
import familyhealth.service.impl.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllDoctors(
            @RequestParam(required = false) String[] search,
            Pageable pageable) {
        try {

            PageResponse<List<DoctorDTO>> pageResponse = doctorService.getAllDoctors(search, pageable);

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(OK.value())
                    .message(MessageKey.GET_LIST_DOCTOR_SUCCESS)
                    .data(pageResponse)
                    .build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getDoctor(@PathVariable Long id) {
        try {
            DoctorDTO doctor = DoctorMapper.convertToDoctorDTO(doctorService.getDoctor(id));

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(OK.value())
                    .message(MessageKey.GET_DOCTOR_SUCCESS)
                    .data(doctor)
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDoctor(@Valid @RequestBody DoctorRegisterDTO doctorRegisterDTO) {
        try {
            doctorService.createDoctor(doctorRegisterDTO);

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(CREATED.value())
                    .message(MessageKey.CREATE_DOCTOR_SUCCESS)
                    .build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDoctor(@Valid @PathVariable Long id,
                                          @RequestBody DoctorDTO doctorDTO) {
        try {
            DoctorDTO doctor = DoctorMapper
                    .convertToDoctorDTO(doctorService.updateDoctor(id, doctorDTO));

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(OK.value())
                    .message(MessageKey.UPDATE_DOCTOR_SUCCESS)
                    .data(doctor)
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .code(NO_CONTENT.value())
                    .message(MessageKey.DELETE_DOCTOR_SUCCESS)
                    .data(null)
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
