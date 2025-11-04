package familyhealth.model.dto.request;

import familyhealth.model.dto.DoctorDTO;
import familyhealth.model.dto.UserDTO;
import lombok.Data;

@Data
public class DoctorRegisterDTO {
    private UserDTO userDTO;
    private DoctorDTO doctorDTO;
}
