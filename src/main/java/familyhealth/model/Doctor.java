package familyhealth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import familyhealth.common.Expertise;
import lombok.experimental.SuperBuilder;
@Getter
@Setter
@Entity
@Table(name = "doctors")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends Person{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Expertise expertise;

    @Column(length = 255, nullable = false)
    private String bio = "";

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
