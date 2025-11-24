package familyhealth.repository;

import familyhealth.common.Relation;
import familyhealth.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    long countByHouseholdIdAndRelation(Long householdId, Relation relation);

    Boolean existsByIdCard(String idCard);
    Boolean existsByBhyt(String bhyt);

    Optional<Member> findByUserId(Long userId);

    List<Member> findAllByHouseholdId(Long householdId);

}
