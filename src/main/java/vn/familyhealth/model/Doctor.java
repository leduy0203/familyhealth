package vn.familyhealth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends User {

    private String specialization;

    private String certificateNumber;

    private String hospitalName;

    private int yearsOfExperience;

    private String bio;
}
