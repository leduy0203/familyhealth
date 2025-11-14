package familyhealth.service;

import familyhealth.model.Doctor;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.UserDTO;
import familyhealth.model.dto.request.DoctorRegisterDTO;


public interface IDoctorService {
    Doctor getDoctor(Long id);
    Doctor createDoctor(DoctorRegisterDTO doctorRegisterDTO);
    Doctor updateDoctor(Long id, DoctorDTO doctorDTO);
    void deleteDoctor(Long id);
}
