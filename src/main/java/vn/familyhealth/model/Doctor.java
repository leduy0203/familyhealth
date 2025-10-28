package vn.familyhealth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "doctor")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends User {

    private String specialization;

    private String certificateNumber;

    private String hospitalName;

    private int yearsOfExperience;

    private String bio;
}
