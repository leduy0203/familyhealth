package familyhealth.service.impl;

import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.DoctorMapper;
import familyhealth.mapper.RoleMapper;
import familyhealth.model.Doctor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import familyhealth.model.Role;
import familyhealth.model.dto.RoleDTO;
import familyhealth.repository.RoleRepository;
import familyhealth.service.IRoleService;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.ROLE_NOT_EXISTED));
    }

    @Override
    public Role createRole(RoleDTO roleDTO) {
        String name = roleDTO.getName();
        if(roleRepository.existsByName(name)){
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = RoleMapper.convertToRole(roleDTO);
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long id, RoleDTO roleDTO) {
        Role existingRole  = getRole(id);
        Role updatedRole = RoleMapper.convertToRole(roleDTO);
        updatedRole.setId(existingRole.getId());

        return roleRepository.save(updatedRole);
    }

    @Override
    public void deleteRole(Long id) {
        Role existingRole  = getRole(id);
        existingRole.setIsActive(false);
        roleRepository.save(existingRole);
    }
}
