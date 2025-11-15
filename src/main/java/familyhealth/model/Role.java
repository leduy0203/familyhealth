package familyhealth.model;

import familyhealth.common.UserType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UserType name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
