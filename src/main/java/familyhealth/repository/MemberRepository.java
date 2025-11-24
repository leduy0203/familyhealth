package familyhealth.repository;

import familyhealth.common.Relation;
import familyhealth.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    long countByHouseholdIdAndRelation(Long householdId, Relation relation);

    Boolean existsByIdCard(String idCard);
    Boolean existsByBhyt(String bhyt);


}
