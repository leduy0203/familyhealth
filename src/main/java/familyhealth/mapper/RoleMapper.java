package familyhealth.mapper;

import familyhealth.model.dto.RoleDTO;
import familyhealth.model.Role;

public class RoleMapper {
    public static Role convertToRole(RoleDTO dto) {
        if (dto == null) return null;

        return Role.builder()
                .name(dto.getName())
                .build();
    }
}
