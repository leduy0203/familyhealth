package vn.familyhealth.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "family")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Family extends AbstractEntity<Integer>{

    private String familyName;

    private String address;
}
