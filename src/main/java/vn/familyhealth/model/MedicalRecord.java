package vn.familyhealth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import vn.familyhealth.common.RecordStatus;

@Entity
@Table(name = "medical_record")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord extends AbstractEntity<Long> {

    private String symptoms;

    private String diagnosis;

    private RecordStatus recordStatus;
}
