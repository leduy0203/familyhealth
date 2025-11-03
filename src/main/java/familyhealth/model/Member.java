package familyhealth.model;

import jakarta.persistence.*;
import lombok.*;
import familyhealth.common.Relation;

@Entity
@Table(name = "members")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation",nullable = false)
    private Relation relation = Relation.CHU_HO;

    @Column(length = 12, nullable = false)
    private String bhyt = "";

    @ManyToOne()
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
