package familyhealth.service;

import familyhealth.model.Doctor;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.UserDTO;


public interface IDoctorService {
    Doctor getDoctor(Long id);
    Doctor createDoctor(DoctorDTO doctorDTO, UserDTO userDTO);
    Doctor updateDoctor(Long id, DoctorDTO doctorDTO);
    void deleteDoctor(Long id);
}
