package familyhealth.service;

import familyhealth.model.Doctor;
import familyhealth.model.User;
import familyhealth.model.dto.DoctorDTO;


public interface IDoctorService {
    Doctor getDoctor(Long id);
    Doctor createDoctor(DoctorDTO doctorDTO, User user);
    Doctor updateDoctor(Long id, DoctorDTO doctorDTO);
    void deleteDoctor(Long id);
}
