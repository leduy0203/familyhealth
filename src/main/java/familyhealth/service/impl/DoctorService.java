package familyhealth.service.impl;

import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.DoctorMapper;
import familyhealth.mapper.UserMapper;
import familyhealth.model.Doctor;
import familyhealth.model.Role;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.UserDTO;
import familyhealth.model.dto.request.DoctorRegisterDTO;
import familyhealth.repository.DoctorRepository;
import familyhealth.repository.UserRepository;
import familyhealth.service.IDoctorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import familyhealth.model.User;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorService implements IDoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.DOCTOR_NOT_FOUND));
    }

    @Override
    public Doctor createDoctor(DoctorRegisterDTO request) {

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        Role role = this.roleService.getRole(request.getRoleId());

        User user = UserMapper.convertToUser(request, role);
        this.userRepository.save(user);
        Doctor doctor = DoctorMapper.convertToDoctor(request, user);
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor updateDoctor(Long id, DoctorDTO doctorDTO) {
        Doctor doctor = getDoctor(id);
        DoctorMapper.mapDtoToEntity(doctorDTO, doctor);
        return doctorRepository.save(doctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor existingDoctor = getDoctor(id);
        userService.deleteUser(existingDoctor.getUser().getId());
        doctorRepository.delete(existingDoctor);
    }
}
