package familyhealth.repository;

import familyhealth.common.Relation;
import familyhealth.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    long countByHouseholdIdAndRelation(Long householdId, Relation relation);

    Boolean existsByIdCard(String idCard);
    Boolean existsByBhyt(String bhyt);

    Optional<Member> findByUserId(Long userId);

    @Query("SELECT m FROM Member m WHERE m.household.id = :householdId AND m.memberStatus = 'ACTIVE'")
    Page<Member> findActiveMembersByHousehold(Long householdId, Pageable pageable);

}
