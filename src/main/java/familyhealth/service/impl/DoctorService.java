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
import familyhealth.model.dto.response.PageResponse;
import familyhealth.repository.DoctorRepository;
import familyhealth.repository.UserRepository;
import familyhealth.repository.specification.DoctorSpecification;
import familyhealth.service.IDoctorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import familyhealth.model.User;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public PageResponse getAllDoctors(String[] search, Pageable pageable) {

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

        return PageResponse.builder()
                .meta(meta)
                .result(doctorDTOs)
                .build();
    }
}
