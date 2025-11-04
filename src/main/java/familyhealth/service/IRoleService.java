package familyhealth.service;

import familyhealth.model.Role;
import familyhealth.model.dto.RoleDTO;

public interface IRoleService {
    Role getRole(Long id);
    Role createRole(RoleDTO roleDTO);
    Role updateRole(Long id, RoleDTO roleDTO);
    void deleteRole(Long id);
}
