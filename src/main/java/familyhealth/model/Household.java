package familyhealth.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "households")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Household{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String address = "";

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
