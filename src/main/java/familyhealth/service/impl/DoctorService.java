package familyhealth.service.impl;

import familyhealth.mapper.DoctorMapper;
import familyhealth.model.Doctor;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.repository.DoctorRepository;
import familyhealth.service.IDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import familyhealth.model.User;

@Service
@RequiredArgsConstructor
public class DoctorService implements IDoctorService {
    private final DoctorRepository doctorRepository;

    @Override
    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Doctor not found"));
    }

    @Override
    public Doctor createDoctor(DoctorDTO doctorDTO, User user) {
        Doctor doctor = DoctorMapper.convertToDotor(doctorDTO, user);
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor updateDoctor(Long id, DoctorDTO doctorDTO) {
        return null;
    }

    @Override
    public void deleteDoctor(Long id) {

    }
}
