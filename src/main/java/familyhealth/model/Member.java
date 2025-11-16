package familyhealth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import familyhealth.common.Relation;
import lombok.experimental.SuperBuilder;
@Data
@Entity
@Table(name = "members")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends Person{
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

    @OneToOne(optional = true )
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
    private User user;
}
