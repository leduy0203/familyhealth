package familyhealth.service.impl;

import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.DoctorMapper;
import familyhealth.model.Doctor;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.UserDTO;
import familyhealth.repository.DoctorRepository;
import familyhealth.service.IDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import familyhealth.model.User;

@Service
@RequiredArgsConstructor
public class DoctorService implements IDoctorService {
    private final DoctorRepository doctorRepository;
    private final UserService userService;

    @Override
    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.DOCTOR_NOT_FOUND));
    }

    @Override
    public Doctor createDoctor(DoctorDTO doctorDTO, UserDTO userDTO) {
        User user = userService.createUser(userDTO);
        Doctor doctor = DoctorMapper.convertToDotor(doctorDTO, user);
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor updateDoctor(Long id, DoctorDTO doctorDTO) {
        Doctor doctor = getDoctor(id);
        Doctor updateDoctor = DoctorMapper.convertToDotor(doctorDTO, doctor.getUser());
        updateDoctor.setId(doctor.getId());
        return doctorRepository.save(updateDoctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor existingDoctor = getDoctor(id);
        userService.deleteUser(existingDoctor.getUser().getId());
        doctorRepository.delete(existingDoctor);
    }
}
