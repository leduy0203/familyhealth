package familyhealth.service.impl;

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
                .orElseThrow(()-> new RuntimeException("Role not found"));
    }

    @Override
    public Role creatRole(RoleDTO roleDTO) {
        String name = roleDTO.getName();
        if(roleRepository.existsByName(name)){
            throw new DataIntegrityViolationException("Role name already exists");
        }
        Role role = new Role();
        role.setName(roleDTO.getName());
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long id, RoleDTO roleDTO) {
        Role role = getRole(id);
        role.setName(roleDTO.getName());
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {

    }
}
