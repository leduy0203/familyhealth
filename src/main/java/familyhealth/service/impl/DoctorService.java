package familyhealth.service.impl;

import familyhealth.common.UserType;
import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.DoctorMapper;
import familyhealth.mapper.UserMapper;
import familyhealth.model.Doctor;
import familyhealth.model.Member;
import familyhealth.model.Role;
import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.UserDTO;
import familyhealth.model.dto.request.DoctorRegisterDTO;
import familyhealth.model.dto.response.PageResponse;
import familyhealth.repository.AppointmentRepository;
import familyhealth.repository.DoctorRepository;
import familyhealth.repository.RoleRepository;
import familyhealth.repository.UserRepository;
import familyhealth.repository.specification.DoctorSpecification;
import familyhealth.service.IDoctorService;
import familyhealth.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import familyhealth.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static familyhealth.common.UserType.DOCTOR;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorService implements IDoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppointmentRepository appointmentRepository;

    @Override
    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.DOCTOR_NOT_FOUND));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(rollbackOn = Exception.class)
    public Doctor createDoctor(DoctorRegisterDTO request) {

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        Role role = roleRepository.findByName(DOCTOR)
                .orElseThrow(()-> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        User user = UserMapper.convertToUser(request, role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
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
    public List<Member> getPatients() {

        String currentPhone = SecurityUtils.getCurrentLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        User currentUser = userService.getUserByPhone(currentPhone);

        List<Member> members = null;

        if (currentUser.getDoctor() != null){
            Long id = currentUser.getDoctor().getId();
            members = appointmentRepository.findPatientsByDoctorId(id);
        }

        return members;

    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor existingDoctor = getDoctor(id);
        userService.deleteUser(existingDoctor.getUser().getId());
        doctorRepository.delete(existingDoctor);
    }

    @Override
    public PageResponse<List<DoctorDTO>> getAllDoctors(String[] search, Pageable pageable) {

        Specification<Doctor> spec = DoctorSpecification.fromSearchCriteria(search);

        Page<Doctor> doctorPage = doctorRepository.findAll(spec, pageable);

        List<DoctorDTO> doctorDTOs = doctorPage.getContent().stream()
                .map(DoctorMapper::convertToDoctorDTO)
                .collect(Collectors.toList());

        PageResponse.Meta meta = new PageResponse.Meta();
        meta.setPage(doctorPage.getNumber());
        meta.setPageSize(doctorPage.getSize());
        meta.setPages(doctorPage.getTotalPages());
        meta.setTotal(doctorPage.getTotalElements());

        return PageResponse.<List<DoctorDTO>>builder()
                .meta(meta)
                .result(doctorDTOs)
                .build();
    }
}
