package familyhealth.common;

import lombok.Data;
import lombok.Getter;

@Getter
public enum UserType {

    ADMIN("ADMIN"),
    DOCTOR("DOCTOR"),
    PATIENT("PATIENT"),
    PATIENT_HOUSEHOLD("PATIENT_HOUSEHOLD");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

}

