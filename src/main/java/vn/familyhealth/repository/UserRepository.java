package vn.familyhealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.familyhealth.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
