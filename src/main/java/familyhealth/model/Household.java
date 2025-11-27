package familyhealth.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "households")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Household{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long househeadId;

    @Column(length = 200, nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "household")
    @JsonIgnore
    private List<Member> member;
}
