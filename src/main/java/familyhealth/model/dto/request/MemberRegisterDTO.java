package familyhealth.model.dto.request;

import familyhealth.model.dto.MemberDTO;
import familyhealth.model.dto.UserDTO;
import lombok.Data;

@Data
public class MemberRegisterDTO {
    private UserDTO userDTO;
    private MemberDTO memberDTO;
    private Long householdId;
}
