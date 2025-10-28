package vn.familyhealth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.*;
import vn.familyhealth.common.Gender;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends AbstractEntity<Long> {

    private String email;

    private String password;

    private String phone;

    private String fullName;

    private String address;

    private Gender gender;

    private LocalDateTime dateOfBirth;

}
