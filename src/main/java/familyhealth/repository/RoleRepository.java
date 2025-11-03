package familyhealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import familyhealth.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);
}