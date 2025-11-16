package familyhealth.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserResponse {

    private String fullName;
    private String email;
    private String phone;
    private String roleName;
    private Long active;
}
