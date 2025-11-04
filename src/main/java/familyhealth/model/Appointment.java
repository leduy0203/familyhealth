package familyhealth.model;

import jakarta.persistence.*;
import lombok.*;
import familyhealth.common.AppointmentStatus;

import java.time.LocalDate;

@Entity
@Table(name = "appointments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate time;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @Column(length = 200, nullable = false)
    private String note = "";

    @ManyToOne()
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne()
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
