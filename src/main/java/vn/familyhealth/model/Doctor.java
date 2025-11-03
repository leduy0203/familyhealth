package vn.familyhealth.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.familyhealth.common.Expertise;

@Entity
@Table(name = "doctors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Expertise expertise;

    @Column(length = 255, nullable = false)
    private String bio = "";

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
