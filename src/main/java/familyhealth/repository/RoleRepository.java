package familyhealth.repository;

import familyhealth.common.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import familyhealth.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(UserType name);

    Optional<Role> findByName(UserType name);
}